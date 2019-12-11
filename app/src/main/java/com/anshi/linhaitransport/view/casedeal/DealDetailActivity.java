package com.anshi.linhaitransport.view.casedeal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseActivity;
import com.anshi.linhaitransport.entry.CaseDetailEntry;
import com.anshi.linhaitransport.entry.DeptEntry;
import com.anshi.linhaitransport.entry.ImageInfo;
import com.anshi.linhaitransport.entry.UploadIvEntry;
import com.anshi.linhaitransport.selfview.CanScorllRecyclerView;
import com.anshi.linhaitransport.selfview.EaseImageView;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.DialogBuild;
import com.anshi.linhaitransport.utils.SharedPreferenceUtils;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.utils.Utils;
import com.anshi.linhaitransport.utils.glide.GlideApp;
import com.anshi.linhaitransport.utils.glide.MyGlideEngine;
import com.anshi.linhaitransport.view.casedeal.adapter.CaseDetailAdapter;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.IThumbViewInfo;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class DealDetailActivity extends BaseActivity implements CaseDetailAdapter.AddEnvClickListener,View.OnClickListener {
    private TextView mContentTv;
    private CanScorllRecyclerView mContentRecycler;
    private CanScorllRecyclerView mDealRecycler;
    private EditText mDealEt;
    private List<String> mDealPhotoList = new ArrayList<>();
    private CaseDetailAdapter caseDetailAdapter;
    public static final int ENV_CODE = 325;
    private int id;
    private LinearLayout mDealLayout;
    private LinearLayout mSelectLayout;
    private Spinner mAreaSpinner;
    private List<DeptEntry.DataBean> areaEntryData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_detail);
        id = getIntent().getIntExtra("id",0);
        initView();
        getDealDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorBlue);
    }
    private TextView replyTv;
    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText("案件办理");
        mContentTv = findViewById(R.id.content_tv);
        mDealEt = findViewById(R.id.deal_et);
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
        caseDetailAdapter = new CaseDetailAdapter(this,mDealPhotoList,this);
        mDealRecycler.setAdapter(caseDetailAdapter);
        mSelectLayout = findViewById(R.id.select_layout);
        mDealLayout = findViewById(R.id.deal_layout);
        mAreaSpinner = findViewById(R.id.part_spinner);
        replyTv= findViewById(R.id.reply_tv);
        replyTv.setOnClickListener(this);
        findViewById(R.id.deal_tv).setOnClickListener(this);
        findViewById(R.id.reverse_tv).setOnClickListener(this);
        completeView();

    }

    private void completeView(){
        String deptName = SharedPreferenceUtils.getString(mContext, "deptName");
        if (deptName.contains("乡镇路长")){
           mDealLayout.setVisibility(View.VISIBLE);
           mSelectLayout.setVisibility(View.GONE);
            findViewById(R.id.reloution_layout).setVisibility(View.VISIBLE);
        }else if (deptName.contains("办公室")){
           mDealLayout.setVisibility(View.GONE);
           mSelectLayout.setVisibility(View.VISIBLE);
           findViewById(R.id.reloution_layout).setVisibility(View.GONE);
           getCaseDeptList();
           addEventListener();
        }else {
            findViewById(R.id.reloution_layout).setVisibility(View.VISIBLE);
            mDealLayout.setVisibility(View.VISIBLE);
            mSelectLayout.setVisibility(View.GONE);
            replyTv.setVisibility(View.GONE);
            findViewById(R.id.divider_view).setVisibility(View.GONE);
        }
    }
    //通用Spinner适配器
    private void initSpinnerData(Spinner spinner, List<String> strings) {

        ArrayAdapter<String> adapterThree = new ArrayAdapter<String>(this,R.layout.dept_spinner, strings);
        spinner.setAdapter(adapterThree);
    }


    private void addEventListener(){
        mAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null!=areaEntryData){
                    mCurrentAreaId = areaEntryData.get(position).getDeptId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private int mCurrentAreaId;
    private void getCaseDeptList(){
        mService.getDeptList()
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
                                DeptEntry areaEntry = gson.fromJson(string, DeptEntry.class);
                                if (areaEntry.getCode()==Constants.SUCCESS_CODE){
                                    if (areaEntry.getData()!=null&&areaEntry.getData().size()>0){
                                        areaEntryData = areaEntry.getData();
                                        List<String> list = new ArrayList<>();
                                        for (int i = 0; i <areaEntryData.size() ; i++) {
                                            list.add(areaEntryData.get(i).getDeptName());
                                        }
                                        initSpinnerData(mAreaSpinner,list);
                                        mCurrentAreaId = areaEntryData.get(0).getDeptId();
                                    }
                                }else {
                                    Toast.makeText(mContext, areaEntry.getMsg(), Toast.LENGTH_SHORT).show();
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
                        GPreviewBuilder.from(DealDetailActivity.this)//activity实例必须
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case ENV_CODE:
                    if (data!=null){
                        List<String> mSelected = Matisse.obtainPathResult(data);
                        if (mSelected.size()>0){
                                lubanImageList(mSelected, new OnLubanFinishListener() {
                                    @Override
                                    public void finish(List<String> newimageList) {
                                        for (int i = 0; i <newimageList.size(); i++) {
                                            uploadIv(newimageList.get(i));
                                        }
                                    }
                                });

                        }

                    }
                    break;
            }
        }
    }


    private void uploadIv(String path){
        final KProgressHUD kProgressHUD = DialogBuild.getBuild().createCommonLoadDialog(this,"正在上传中");
        JSONObject jsonObject = new JSONObject();
        try {
            String fileToBase64 = Utils.fileToBase64(path);
            jsonObject.put("base64",fileToBase64);
            jsonObject.put("fileName",new File(path).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        mService.uploadImage(requestBody)
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
                        if (null!=kProgressHUD){
                            kProgressHUD.dismiss();
                        }
                        try {
                            String string = responseBody.string();
                            if (Utils.isGoodJson(string)){
                                Gson gson = new Gson();
                                UploadIvEntry uploadIvEntry = gson.fromJson(string, UploadIvEntry.class);
                                if (uploadIvEntry.getCode()==Constants.SUCCESS_CODE){
                                    Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                                    mDealPhotoList.add(uploadIvEntry.getData());
                                    caseDetailAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(mContext, uploadIvEntry.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (null!=kProgressHUD){
                            kProgressHUD.dismiss();
                        }
                        throwable.printStackTrace();
                    }
                });
    }


    @Override
    public void addDeleEnvClick(int position) {
        mDealPhotoList.remove(position);
        caseDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void addEnvClick(boolean isClick, int position) {
        if (isClick){
            callGallery(ENV_CODE,9-mDealPhotoList.size());
        } else {
                List<IThumbViewInfo> mImageList = new ArrayList<>();
                for (int i = 0; i <mDealPhotoList.size() ; i++) {
                    String photoPath = mDealPhotoList.get(i);
                    mImageList.add(new ImageInfo(Constants.IMAGE_URL+photoPath));

                }
                GPreviewBuilder.from(this)//activity实例必须
                        .setData(mImageList)//集合
                        .setCurrentIndex(position)
                        .setSingleFling(true)//是否在黑屏区域点击返回
                        .setDrag(false)//是否禁用图片拖拽返回
                        .setType(GPreviewBuilder.IndicatorType.Number)//指示器类型
                        .start();//启动
            }

        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reply_tv:
                createReflectDealDialog();
                break;
            case R.id.deal_tv:
                disposeDealCase();
                break;
            case R.id.reverse_tv:
                reverseCase();
                break;

        }
    }

    private void disposeDealCase(){
        if (TextUtils.isEmpty(mDealEt.getText())){
            Toast.makeText(mContext, "请输入处理意见", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isFinishing()){
            commonLoadDialog = DialogBuild.getBuild().createCommonLoadDialog(this,"正在处理");
        }

        String filePath = null;
        if (mDealPhotoList.size()>0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <mDealPhotoList.size() ; i++) {
                stringBuilder.append(mDealPhotoList.get(i)).append(",");
            }
            filePath = stringBuilder.substring(0,stringBuilder.length()-1);
        }
        int deptId = SharedPreferenceUtils.getInt(mContext, "deptId");
        mService.disPoseCase(id,mDealEt.getText().toString(),deptId,filePath)
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
                                        new Handler(Looper.getMainLooper())
                                                .postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                      setResult(RESULT_OK);
                                                      finish();
                                                    }
                                                },1000);
                                    }else {
                                        Toast.makeText(mContext,msg , Toast.LENGTH_SHORT).show();
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



    private void reverseCase(){
        if (!isFinishing()){
            commonLoadDialog = DialogBuild.getBuild().createCommonLoadDialog(this,"正在转发");
        }
        mService.forwardCase(id,mCurrentAreaId)
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
                                        Toast.makeText(mContext, "转发成功", Toast.LENGTH_SHORT).show();
                                        new Handler(Looper.getMainLooper())
                                                .postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                      setResult(RESULT_OK);
                                                      finish();
                                                    }
                                                },1000);
                                    }else {
                                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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





    private void createReflectDealDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提醒")
                .setMessage("确定像上级部门反映吗?")
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
                        reflectDeal(id);
                    }
                })
                .create();
        if (!isFinishing()){
            alertDialog.show();
        }
    }


    private void reflectDeal(int caseId){
        mService.reflectCase(caseId)
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
                            try {
                                JSONObject jsonObject = new JSONObject(string);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code==Constants.SUCCESS_CODE){
                                    Toast.makeText(mContext, "已向上级反映", Toast.LENGTH_SHORT).show();
                                    new Handler(Looper.getMainLooper())
                                            .postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    setResult(RESULT_OK);
                                                    finish();
                                                }
                                            },2000);
                                }else {
                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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




    public interface OnLubanFinishListener {
        void finish(List<String> newimageList);
    }

    private KProgressHUD commonLoadDialog;
    //鲁班压缩
    private void lubanImageList(final List<String> imageListOrigin, final OnLubanFinishListener listener) {
        if (!isFinishing()){
            commonLoadDialog = DialogBuild.getBuild().createCommonLoadDialog(this,"正在压缩");
        }
        final List<String> imageList = (List<String>) ((ArrayList) imageListOrigin).clone();
        final List<String> newimageList= (List<String>) new ArrayList<>().clone();
        for (int i = 0; i<imageList.size(); i++){
            //在这里做图片压缩
            //得到newImagelist
            final int index=i;
            File oldFile=new File(imageList.get(i));
            Luban.with(mContext) // 初始化
                    .load(oldFile) // 要压缩的图片
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File newFile) {
                            // 压缩成功后调用，返回压缩后的图片文件
                            // 获取返回的图片地址 newfile
                            String newPath=newFile.getAbsolutePath();
                            newimageList.add(index,newPath);
                            // 输出图片的大小
                            if (newimageList.size()==imageList.size()){
                                if (null!=commonLoadDialog){
                                    commonLoadDialog.dismiss();
                                }
                                listener.finish(newimageList);
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, "压缩出错", Toast.LENGTH_SHORT).show();

                        }
                    }).launch(); // 启动压缩
        }
    }
    /**
     * 调用图库选择
     */
    private void callGallery(int code,int size){
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))//照片视频全部显示MimeType.allOf()
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(size)//最大选择数量为9
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))//图片显示表格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//图像选择和预览活动所需的方向
                .thumbnailScale(0.85f)//缩放比例
                .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new MyGlideEngine())//图片加载方式，Glide4需要自定义实现
                .capture(true) //是否提供拍照功能，兼容7.0系统需要下面的配置
                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .captureStrategy(new CaptureStrategy(true,"com.anshi.linhaitransport.zhihu.fileprovider"))//存储到哪里
                .forResult(code);//请求码
    }

}
