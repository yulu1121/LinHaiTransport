package com.anshi.linhaitransport.view.casedeal;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseActivity;
import com.anshi.linhaitransport.entry.CaseDetailEntry;
import com.anshi.linhaitransport.entry.ImageInfo;
import com.anshi.linhaitransport.selfview.CanScorllRecyclerView;
import com.anshi.linhaitransport.selfview.EaseImageView;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.utils.Utils;
import com.anshi.linhaitransport.utils.glide.GlideApp;
import com.google.gson.Gson;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.IThumbViewInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RoadPersonActivity extends BaseActivity {
    private TextView mContentTv;
    private CanScorllRecyclerView mContentRecycler;
    private CanScorllRecyclerView mDealRecycler;
    private TextView mDealTv;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_deal_case);
        id = getIntent().getIntExtra("id",0);
        initView();
        getDealDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorBlue);
    }



    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText("案件办理");
        mContentTv = findViewById(R.id.content_tv);
        mDealTv = findViewById(R.id.deal_tv);
        mContentRecycler = findViewById(R.id.detail_recycler);
        mContentRecycler.setLayoutManager(new GridLayoutManager(this,3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mDealRecycler = findViewById(R.id.deal_recycler);
        mDealRecycler.setLayoutManager(new GridLayoutManager(this,3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

    }

    private void getDealDetail(){
        mService.findCaseById(id)
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
                        try {
                            String string = responseBody.string();
                            if (Utils.isGoodJson(string)){
                                Gson gson = new Gson();
                                CaseDetailEntry caseDetailEntry = gson.fromJson(string, CaseDetailEntry.class);
                                if (caseDetailEntry.getCode()== Constants.SUCCESS_CODE){
                                    if (null!=caseDetailEntry.getData()){
                                        completeRecycler(caseDetailEntry.getData());
                                    }
                                }else {
                                    Toast.makeText(mContext, caseDetailEntry.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void completeRecycler(CaseDetailEntry.DataBean dataBean){
        final List<String> mList = new ArrayList<>();
        if (dataBean.getCasedata().getFilepaths()!=null) {
            if ( dataBean.getCasedata().getFilepaths().contains(",")){
                String[] split = dataBean.getCasedata().getFilepaths().split(",");
                mList.addAll(Arrays.asList(split));
            }else {
                mList.add(dataBean.getCasedata().getFilepaths());
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        CaseDetailEntry.DataBean.CasedataBean casedata = dataBean.getCasedata();
        stringBuilder.append("标题:").append(casedata.getCaseTitle()).append("\n").append("地点:").append(casedata.getAddress()).append("\n").append("上报时间:").append(casedata.getCreateDate()).append("\n").append("详情:").append(casedata.getDescription());
        mContentTv.setText(stringBuilder.toString());
        CommonAdapter<String> commonAdapter = new CommonAdapter<String>(this,R.layout.item_iv_publish,mList) {
            @Override
            protected void convert(ViewHolder holder, String integer, final int position) {
                EaseImageView easeImageView = holder.getView(R.id.detail_iv);
                ImageView deleteIv = holder.getView(R.id.delete_iv);
                deleteIv.setVisibility(View.GONE);
                GlideApp.with(mContext).load(Constants.IMAGE_URL+integer).centerCrop().error(R.drawable.ease_default_image).into(easeImageView);
                easeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<IThumbViewInfo> mImageList = new ArrayList<>();
                        for (int i = 0; i <mList.size() ; i++) {
                            String photoPath = mList.get(i);
                            mImageList.add(new ImageInfo(Constants.IMAGE_URL+photoPath));

                        }
                        GPreviewBuilder.from(RoadPersonActivity.this)//activity实例必须
                                .setData(mImageList)//集合
                                .setCurrentIndex(position)
                                .setSingleFling(true)//是否在黑屏区域点击返回
                                .setDrag(false)//是否禁用图片拖拽返回
                                .setType(GPreviewBuilder.IndicatorType.Number)//指示器类型
                                .start();//启动
                    }
                });
            }
        };
        mContentRecycler.setAdapter(commonAdapter);
        //返回意见开始
        if (dataBean.getDisposedata()!=null){
            String disposeOpinion = dataBean.getDisposedata().getDisposeOpinion();
            mDealTv.setText(disposeOpinion);
            if (dataBean.getDisposedata().getFilePath()!=null){
                final List<String> stringList = new ArrayList<>();
                if (dataBean.getDisposedata().getFilePath().contains(",")){
                    stringList.addAll(Arrays.asList(dataBean.getDisposedata().getFilePath().split(",")));
                }else {
                    stringList.add(dataBean.getDisposedata().getFilePath());
                }
                CommonAdapter<String> commonAdapterOne = new CommonAdapter<String>(this,R.layout.item_iv_publish,stringList) {
                    @Override
                    protected void convert(ViewHolder holder, String integer, final int position) {
                        EaseImageView easeImageView = holder.getView(R.id.detail_iv);
                        ImageView deleteIv = holder.getView(R.id.delete_iv);
                        deleteIv.setVisibility(View.GONE);
                        GlideApp.with(mContext).load(Constants.IMAGE_URL+integer).centerCrop().error(R.drawable.ease_default_image).into(easeImageView);
                        easeImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<IThumbViewInfo> mImageList = new ArrayList<>();
                                for (int i = 0; i <stringList.size() ; i++) {
                                    String photoPath = stringList.get(i);
                                    mImageList.add(new ImageInfo(Constants.IMAGE_URL+photoPath));

                                }
                                GPreviewBuilder.from(RoadPersonActivity.this)//activity实例必须
                                        .setData(mImageList)//集合
                                        .setCurrentIndex(position)
                                        .setSingleFling(true)//是否在黑屏区域点击返回
                                        .setDrag(false)//是否禁用图片拖拽返回
                                        .setType(GPreviewBuilder.IndicatorType.Number)//指示器类型
                                        .start();//启动
                            }
                        });
                    }
                };
                mDealRecycler.setAdapter(commonAdapterOne);
            }
        }else {
            if (dataBean.getCasedata().getPatrolType().equals("2")){
                findViewById(R.id.solution_tv).setVisibility(View.GONE);
                mDealTv.setVisibility(View.GONE);
            }else {
                mDealTv.setText("请等待回复意见");
            }

        }
        //返回意见结束



    }





}
