package com.anshi.linhaitransport.view.casedeal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseActivity;
import com.anshi.linhaitransport.entry.CaseListEntry;
import com.anshi.linhaitransport.selfview.EaseImageView;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.DialogBuild;
import com.anshi.linhaitransport.utils.SharedPreferenceUtils;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.utils.Utils;
import com.anshi.linhaitransport.utils.glide.GlideApp;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CaseDealActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private List<CaseListEntry.DataBean> mList = new ArrayList<>();
    private CommonAdapter<CaseListEntry.DataBean> commonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        initView();
        completeRecycler();
        addEventListener();
        findCaseList(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorBlue);
    }

    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText("案件列表");
        mRefreshLayout = findViewById(R.id.smart_refresh);
        mRecyclerView = findViewById(R.id.case_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }
    private KProgressHUD commonLoadDialog;
    private void findCaseList(final boolean refresh){
        if (!isFinishing()){
            commonLoadDialog = DialogBuild.getBuild().createCommonLoadDialog(this,"正在加载");
        }
        int userId = SharedPreferenceUtils.getInt(this, "userId");
        mService.findCase(String.valueOf(userId))
                .map(new Func1<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody call(ResponseBody responseBody) {
                        return responseBody;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        if (null!=commonLoadDialog){
                            commonLoadDialog.dismiss();
                        }
                        try {
                            String string = responseBody.string();
                            if (Utils.isGoodJson(string)){
                                Gson gson = new Gson();
                                CaseListEntry caseListEntry = gson.fromJson(string, CaseListEntry.class);
                                if (caseListEntry.getCode()== Constants.SUCCESS_CODE){
                                    if (refresh){
                                        mRefreshLayout.finishRefresh(true);
                                    }
                                    if (caseListEntry.getData()!=null){
                                        mList.clear();
                                        mList.addAll(caseListEntry.getData());
                                        commonAdapter.notifyDataSetChanged();
                                    }
                                }else {
                                    if (refresh){
                                        mRefreshLayout.finishRefresh(false);
                                    }
                                    Toast.makeText(mContext, caseListEntry.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (null!=commonLoadDialog){
                            commonLoadDialog.dismiss();
                        }
                        if (refresh){
                            mRefreshLayout.finishRefresh(false);
                        }
                        throwable.printStackTrace();
                    }
                });
    }


    private void addEventListener(){
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                   findCaseList(true);
            }
        });
    }
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private void completeRecycler(){

         commonAdapter = new CommonAdapter<CaseListEntry.DataBean>(this,R.layout.item_deal_case,mList) {
            @Override
            protected void convert(ViewHolder holder, final CaseListEntry.DataBean dataBean, int position) {
                EaseImageView easeImageView = holder.getView(R.id.item_iv);
                String path = "";
                if (dataBean.getFiles()!=null){
                    if (dataBean.getFiles().contains(",")){
                        String[] strings = dataBean.getFiles().split(",");
                        path = strings[0];
                    }else {
                        path = dataBean.getFiles();
                    }
                }
                GlideApp.with(mContext).load(Constants.IMAGE_URL+path).centerCrop().error(R.drawable.ease_default_image).into(easeImageView);
                TextView addressTv = holder.getView(R.id.item_address_tv);
                addressTv.setText(dataBean.getAddress());
                TextView dateTv = holder.getView(R.id.item_date_tv);
                TextView weekTv = holder.getView(R.id.item_week_tv);
                TextView hourTv = holder.getView(R.id.item_time_tv);
                TextView contentTv = holder.getView(R.id.item_content_tv);
                final Button itemBtn = holder.getView(R.id.item_btn);
                Button otherBtn = holder.getView(R.id.other_btn);
                if (dataBean.getCase_state().equals("1")){
                        itemBtn.setText("上报");
                        otherBtn.setText("删除");
                    }else {
                        itemBtn.setText("已上报");
                        otherBtn.setVisibility(View.GONE);
                }

                try {
                    Date parse = simpleDateFormat.parse(dataBean.getCreate_date());
                    String date = Utils.getTime(parse);
                    dateTv.setText(date);
                    String week = Utils.getWeek(dataBean.getCreate_date());
                    weekTv.setText(week);
                    hourTv.setText(Utils.getOnlyHourMinTime(parse));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                contentTv.setText(dataBean.getDescription());
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,RoadPersonActivity.class);
                        intent.putExtra("id",dataBean.getId());
                        startActivity(intent);
                    }
                });
                itemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemBtn.getText().toString().equals("上报")){
                            createChangeDialog("上报","上报后将不可修改",dataBean.getId());
                        }else {
                            Intent intent = new Intent(mContext,RoadPersonActivity.class);
                            intent.putExtra("id",dataBean.getId());
                            startActivity(intent);
                        }
                    }
                });
                otherBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createChangeDialog("删除","确定删除吗?",dataBean.getId());
                    }
                });
            }
        };
        mRecyclerView.setAdapter(commonAdapter);
    }

    private void createChangeDialog(final String title, String messge, final int caseId){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(messge)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                    }
                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (title.equals("上报")){
                                uploadCase(caseId);
                            }else {
                                deleteCase(caseId);
                            }
                    }
                })
                .create();
        if (!isFinishing()){
            alertDialog.show();
        }
    }


    private void deleteCase(int caseId){
        if (!isFinishing()){
            commonLoadDialog = DialogBuild.getBuild().createCommonLoadDialog(this,"正在加载");
        }
        mService.caseDel(caseId)
                .map(new Func1<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody call(ResponseBody responseBody) {
                        return responseBody;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                            if (null!=commonLoadDialog){
                                commonLoadDialog.dismiss();
                            }
                        try {
                            String string = responseBody.string();
                            if (Utils.isGoodJson(string)){
                                try {
                                    JSONObject jsonObject = new JSONObject(string);
                                    int code = jsonObject.getInt("code");
                                    String msg = jsonObject.getString("msg");
                                    if (code==Constants.SUCCESS_CODE){
                                        Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                                        findCaseList(false);
                                    }else {
                                        Toast.makeText(mContext,msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (null!=commonLoadDialog){
                            commonLoadDialog.dismiss();
                        }
                        throwable.printStackTrace();
                    }
                });
    }

    private void uploadCase(int caseId){
        if (!isFinishing()){
            commonLoadDialog = DialogBuild.getBuild().createCommonLoadDialog(this,"正在加载");
        }
        int userId = SharedPreferenceUtils.getInt(this, "userId");
        mService.saveCase(caseId,userId)
                .map(new Func1<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody call(ResponseBody responseBody) {
                        return responseBody;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        if (null!=commonLoadDialog){
                            commonLoadDialog.dismiss();
                        }
                        try {
                            String string = responseBody.string();
                            Log.e("xxx",string);
                            if (Utils.isGoodJson(string)){
                                try {
                                    JSONObject jsonObject = new JSONObject(string);
                                    int code = jsonObject.getInt("code");
                                    String msg = jsonObject.getString("msg");
                                    if (code==Constants.SUCCESS_CODE){
                                        Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                                        findCaseList(true);
                                    }else {
                                        Toast.makeText(mContext,msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (null!=commonLoadDialog){
                            commonLoadDialog.dismiss();
                        }
                        throwable.printStackTrace();
                    }
                });

    }

}
