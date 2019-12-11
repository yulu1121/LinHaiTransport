package com.anshi.linhaitransport.view.tourmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseApplication;
import com.anshi.linhaitransport.entry.AreaEntry;
import com.anshi.linhaitransport.entry.CaseTypeEntry;
import com.anshi.linhaitransport.entry.ImageInfo;
import com.anshi.linhaitransport.entry.UploadIvEntry;
import com.anshi.linhaitransport.net.AppHttpService;
import com.anshi.linhaitransport.selfview.EaseImageView;
import com.anshi.linhaitransport.utils.BitmapUtils;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.DialogBuild;
import com.anshi.linhaitransport.utils.SDCardUtil;
import com.anshi.linhaitransport.utils.SharedPreferenceUtils;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.utils.ToastUtils;
import com.anshi.linhaitransport.utils.Utils;
import com.anshi.linhaitransport.utils.WeakHandler;
import com.anshi.linhaitransport.utils.glide.GlideApp;
import com.anshi.linhaitransport.utils.gpsutils.Gps;
import com.anshi.linhaitransport.utils.watermask.WaterMaskUtil;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.IThumbViewInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.TResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TourManagerActivity extends TakePhotoActivity implements View.OnClickListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private static final String CUSTOM_FILE_NAME_GRAY = "custom_map_config.json";
    private TextView mAddressTv;
    private TextView mTimeTv;
    private RelativeLayout mUploadIvLayout;
    private LinearLayout mUploadFormationLayout;
    private Context mContext;
    private TakePhoto takePhoto;
    private Uri outUri;
    private Gps gps;
    private String mCurrentAddress;
    private Button mCaseUploadBtn;
    private Spinner mCaseTypeSpinner;
    private Spinner mCaseAreaSpinner;
    //图片
    private List<String> mImagePathList = new ArrayList<>();
    private CommonAdapter<String> commonAdapter;
    private RecyclerView mImageRecycler;
    private WeakHandler weakHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 22:
                    mTimeTv.setText(Utils.getSecondTime(new Date()));
                    break;
            }
            return true;
        }
    });
    private KProgressHUD commonLoadDialog;
    private TextView mBeginTourTv;
    private AppHttpService mService;
    private List<CaseTypeEntry.DataBean> caseTypeEntryData;
    private EditText mCaseTitleEt;
    private List<AreaEntry.DataBean> areaEntryData;
    private EditText mRoadEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_tour_manager);
        takePhoto = getTakePhoto();
        mService = BaseApplication.getInstances().getAppRetrofit().create(AppHttpService.class);
        initView();
        initLocation();
        getCaseTypeList();
        getCaseArea();
        addEventListener();
    }
    //通用Spinner适配器
    private void initSpinnerData(Spinner spinner, List<String> strings) {

        ArrayAdapter<String> adapterThree = new ArrayAdapter<String>(this,R.layout.item_spinner, strings);
        spinner.setAdapter(adapterThree);
    }

    private void executeTime(){
        weakHandler.post(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 22;
                weakHandler.sendMessage(message);
                weakHandler.postDelayed(this,1000);
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        addWaterMask(BitmapUtils.amendRotatePhoto(result.getImage().getCompressPath()));
    }
    private int mCurrentAreaId;
    private void getCaseArea(){
        mService.getArea()
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
                                AreaEntry areaEntry = gson.fromJson(string, AreaEntry.class);
                                if (areaEntry.getCode()==Constants.SUCCESS_CODE){
                                    if (areaEntry.getData()!=null&&areaEntry.getData().size()>0){
                                        areaEntryData = areaEntry.getData();
                                        List<String> list = new ArrayList<>();
                                        for (int i = 0; i <areaEntryData.size() ; i++) {
                                            list.add(areaEntryData.get(i).getAreaName());
                                        }
                                        initSpinnerData(mCaseAreaSpinner,list);
                                        mCurrentAreaId = areaEntryData.get(0).getAreaId();
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




    private void selectFile(){
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            if (mkdirs){
                Log.d("xxx","创建成功");
            }else {
                Log.d("xxx","文件已存在");
            }
        }
        outUri= Uri.fromFile(file);
        CompressConfig compressConfig=new CompressConfig.Builder().setMaxSize(50*1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig,true);
        takePhoto.onPickFromCapture(outUri);
    }

    private void addWaterMask(Bitmap sourceBitmap){
        StringBuilder stringBuilder = new StringBuilder();
        if (null!=gps){
            stringBuilder.append("经度:").append(gps.getWgLon()).append("\n");
            stringBuilder.append("纬度:").append(gps.getWgLat()).append("\n");
        }
        if (!TextUtils.isEmpty(mCurrentAddress)){
            stringBuilder.append("地址:").append(mCurrentAddress).append("\n");
        }
        stringBuilder.append("时间:").append(Utils.getSecondTime(new Date()));

        Bitmap waterMaskLeftBottom = WaterMaskUtil.drawTextToLeftBottom(this, sourceBitmap, stringBuilder.toString(), 6, Color.WHITE,5, 40);
        findViewById(R.id.around_layout).setVisibility(View.VISIBLE);
        String saveToSdCardOne = SDCardUtil.saveToSdCard(waterMaskLeftBottom);
        uploadIv(saveToSdCardOne);
    }
    @Override
    public void takeFail(TResult result, String msg) {

        Toast.makeText(mContext,msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();

    }

    private int mCurrentCaseId;

    /**
     * 获取案件类型
     */
    private void getCaseTypeList(){
        mService.getCaseType()
                .map(new Func1<ResponseBody,ResponseBody>() {
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
                                CaseTypeEntry caseTypeEntry = gson.fromJson(string, CaseTypeEntry.class);
                                if (caseTypeEntry.getCode()==Constants.SUCCESS_CODE){
                                    if (caseTypeEntry.getData()!=null&&caseTypeEntry.getData().size()>0){
                                        caseTypeEntryData = caseTypeEntry.getData();
                                        List<String> mList = new ArrayList<>();
                                        for (int i = 0; i <caseTypeEntryData.size(); i++) {
                                            mList.add(caseTypeEntryData.get(i).getTypeName());
                                        }
                                        initSpinnerData(mCaseTypeSpinner,mList);
                                        mCurrentCaseId = caseTypeEntryData.get(0).getId();
                                    }
                                }else {
                                    Toast.makeText(mContext, caseTypeEntry.getMsg(), Toast.LENGTH_SHORT).show();
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

    private void addEventListener(){
        mCaseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (null!=caseTypeEntryData){
                        mCurrentCaseId = caseTypeEntryData.get(position).getId();
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCaseAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null!=areaEntryData){
                    mCurrentAreaId = areaEntryData.get(position).getAreaId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    /**
     * 初始化控件
     */
    private void initView() {
        TextView title = findViewById(R.id.title_tv);
        mBeginTourTv = findViewById(R.id.begin_tour);
        mBeginTourTv.setOnClickListener(this);
        if (SharedPreferenceUtils.getString(mContext,"deptName").contains("巡路员")){
            title.setText("巡检");
            mBeginTourTv.setText("巡查开始");
        }else {
            title.setText("养护");
            mBeginTourTv.setText("养护开始");
        }
        mMapView = findViewById(R.id.map_view);
        // 不显示百度地图Logo
        mMapView.removeViewAt(1);
        String customStyleFilePath = getCustomStyleFilePath(this, CUSTOM_FILE_NAME_GRAY);
        // 设置个性化地图样式文件的路径和加载方式
        mMapView.setMapCustomStylePath(customStyleFilePath);
        // 动态设置个性化地图样式是否生效
        mMapView.setMapCustomStyleEnable(true);
        mBaiduMap = mMapView.getMap();
        MapStatus mMapStatus = new MapStatus.Builder().zoom(15.0f).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.setMyLocationEnabled(true);
        mCaseAreaSpinner = findViewById(R.id.area_spinner);
        mCaseTitleEt= findViewById(R.id.title_et);
        mRoadEt = findViewById(R.id.road_et);
        mCaseTypeSpinner = findViewById(R.id.case_spinner);
        mAddressTv = findViewById(R.id.real_address_tv);
        mTimeTv = findViewById(R.id.real_time_tv);
        mTimeTv.setText(Utils.getSecondTime(new Date()));

        mUploadIvLayout = findViewById(R.id.upload_iv_layout);
        mUploadIvLayout.setOnClickListener(this);
        mUploadFormationLayout = findViewById(R.id.upload_layout);
        mCaseUploadBtn = findViewById(R.id.case_upload_btn);
        mCaseUploadBtn.setOnClickListener(this);
        mImageRecycler = findViewById(R.id.image_recycler);
        mImageRecycler.setLayoutManager(new GridLayoutManager(mContext,5));
        commonAdapter = new CommonAdapter<String>(this,R.layout.item_image,mImagePathList) {
            @Override
            protected void convert(ViewHolder holder, final String s, final int position) {
                EaseImageView imageView = holder.getView(R.id.around_iv);
                ImageView deleteIv = holder.getView(R.id.delete_iv);
                GlideApp.with(mContext).load(Constants.IMAGE_URL+s).error(R.drawable.ease_default_image).centerCrop().into(imageView);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<IThumbViewInfo> mImageList = new ArrayList<>();
                        for (int i = 0; i <mImagePathList.size() ; i++) {
                            String photoPath = mImagePathList.get(i);
                            mImageList.add(new ImageInfo(Constants.IMAGE_URL+photoPath));
                        }
                        GPreviewBuilder.from(TourManagerActivity.this)//activity实例必须
                                .setData(mImageList)//集合
                                .setCurrentIndex(position)
                                .setSingleFling(true)//是否在黑屏区域点击返回
                                .setDrag(false)//是否禁用图片拖拽返回
                                .setType(GPreviewBuilder.IndicatorType.Number)//指示器类型
                                .start();//启动
                    }
                });
                deleteIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImagePathList.remove(position);
                        commonAdapter.notifyDataSetChanged();
                        if (mImagePathList.size()==0){
                            findViewById(R.id.around_layout).setVisibility(View.GONE);
                        }

                    }
                });
            }
        };
        mImageRecycler.setAdapter(commonAdapter);
    }
    private TranslateAnimation mShowAction;

    private void initAnimation() {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
    }

    private void uploadTourData(){
        if (TextUtils.isEmpty(mCaseTitleEt.getText())){
            Toast.makeText(mContext, "请输入案件标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImagePathList.size()==0){
            Toast.makeText(mContext, "请至少拍一张照片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mRoadEt.getText())){
            Toast.makeText(mContext, "请输入事件描述", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isFinishing()){
            commonLoadDialog = DialogBuild.getBuild().createCommonLoadDialog(this,"正在上传");
        }
        StringBuilder stringBuilder = new StringBuilder();
        int userId = SharedPreferenceUtils.getInt(mContext, "userId");
        for (int i = 0; i <mImagePathList.size() ; i++) {
            stringBuilder.append(mImagePathList.get(i)).append(",");
        }
        String filePath = stringBuilder.substring(0, stringBuilder.length() - 1);
        mService.addCase(mCaseTitleEt.getText().toString(),String.valueOf(mCurrentCaseId),
                mRoadEt.getText().toString(),String.valueOf(mCurrentAreaId),String.valueOf(userId),String.valueOf(gps.getWgLon()),String.valueOf(gps.getWgLat()),mCurrentAddress,filePath)
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
                                        Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                                        initAnimation();
                                        mRoadEt.setText(null);
                                        mCaseTitleEt.setText(null);
                                        mUploadFormationLayout.setVisibility(View.GONE);
                                        mBeginTourTv.setAnimation(mShowAction);
                                        mBeginTourTv.setVisibility(View.VISIBLE);
                                        findViewById(R.id.around_layout).setVisibility(View.GONE);
                                        mImagePathList.clear();
                                        commonAdapter.notifyDataSetChanged();
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


    /**
     * 读取json路径
     */
    private String getCustomStyleFilePath(Context context, String customStyleFileName) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String parentPath = null;
        try {
            inputStream = context.getAssets().open("customConfigdir/" + customStyleFileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            parentPath = context.getFilesDir().getAbsolutePath();
            File customStyleFile = new File(parentPath + "/" + customStyleFileName);
            if (customStyleFile.exists()) {
                customStyleFile.delete();
            }
            customStyleFile.createNewFile();

            outputStream = new FileOutputStream(customStyleFile);
            outputStream.write(buffer);
        } catch (IOException e) {
            Log.e("CustomMapDemo", "Copy custom style file failed", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e("CustomMapDemo", "Close stream failed", e);
                return null;
            }
        }
        return parentPath + "/" + customStyleFileName;
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        mBaiduMap.setMyLocationEnabled(true);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorBlue);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //关闭定位
        mBaiduMap.setMyLocationEnabled(false);
        if(mLocationClient.isStarted()){
            mLocationClient.stop();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduMap.clear();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        weakHandler.removeCallbacksAndMessages(null);

    }
    private boolean mTourStart;

    public void back(View view){
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.begin_tour:
                if (mBeginTourTv.getText().toString().equals("巡查开始")||mBeginTourTv.getText().toString().equals("养护开始")){
                    executeTime();
                    if (SharedPreferenceUtils.getString(mContext,"deptName").contains("巡路员")){
                        mBeginTourTv.setText("巡查结束");
                    }else {
                        mBeginTourTv.setText("养护结束");
                    }
                    mBeginTourTv.setBackgroundResource(R.drawable.tour_end_shape);
                    mTourStart = true;
                    mUploadIvLayout.setVisibility(View.VISIBLE);
                }else {
                    weakHandler.removeCallbacksAndMessages(null);
                    if (SharedPreferenceUtils.getString(mContext,"deptName").contains("巡路员")){
                        mBeginTourTv.setText("巡查开始");
                    }else {
                        mBeginTourTv.setText("养护开始");
                    }
                    mTourStart = false;
                    uploadLocation();
                    mBeginTourTv.setBackgroundResource(R.drawable.tour_shape);
                    mUploadIvLayout.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.upload_iv_layout:
                if (mUploadFormationLayout.getVisibility()==View.VISIBLE){
                    initAnimation();
                    mUploadFormationLayout.setVisibility(View.GONE);
                    mBeginTourTv.setAnimation(mShowAction);
                    mBeginTourTv.setVisibility(View.VISIBLE);
                }else {
                    initAnimation();
                    mBeginTourTv.setVisibility(View.GONE);
                    mUploadFormationLayout.setVisibility(View.VISIBLE);
                    mUploadFormationLayout.setAnimation(mShowAction);

                }
                break;
            case R.id.case_upload_btn:
                uploadTourData();
//                initAnimation();
//                mUploadFormationLayout.setVisibility(View.GONE);
//                mBeginTourTv.setAnimation(mShowAction);
//                mBeginTourTv.setVisibility(View.VISIBLE);
//                findViewById(R.id.around_layout).setVisibility(View.GONE);
//                mImagePathList.clear();
//                commonAdapter.notifyDataSetChanged();
                break;

        }
    }

    public void takePhoto(View view) {
        selectFile();
    }

    //自定义的定位监听
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //将获取的location信息给百度map
            if (location == null || mMapView == null){
                return;
            }
            if (null!=commonLoadDialog){
                commonLoadDialog.dismiss();
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            gps = new Gps(location.getLatitude(),location.getLongitude());
//            SharedPreferenceUtils.saveFloat(mContext,Constants.CURRENT_LATITUDE, (float) location.getLatitude());
//            SharedPreferenceUtils.saveFloat(mContext,Constants.CURRENT_LONGTITUDE, (float) location.getLongitude());
            mCurrentAddress = location.getAddrStr();
            mAddressTv.setText(location.getAddrStr());
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.setMapStatus(status);//直接到中间
            if (mTourStart){
                uploadLocation();
            }
        }
    }
    private int index = 0;
    private void uploadLocation(){
        Map<String,String> keyMap = new HashMap<>();
        int areaId = SharedPreferenceUtils.getInt(mContext, "areaId");
        int userId = SharedPreferenceUtils.getInt(mContext, "userId");
        keyMap.put("areaid",String.valueOf(areaId));
        keyMap.put("userid",String.valueOf(userId));
        index++;
        if (mTourStart){
            if (index==1){
                keyMap.put("start","1");
            }
        }else {
            index = 0;
            keyMap.put("end","1");
        }
        keyMap.put("longitude",String.valueOf(gps.getWgLon()));
        keyMap.put("latitude",String.valueOf(gps.getWgLat()));
        mService.uploadLocation(keyMap)
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
                                try {
                                    JSONObject jsonObject = new JSONObject(string);
                                    int code = jsonObject.getInt("code");
                                    if (code==Constants.SUCCESS_CODE){
                                        ToastUtils.showShortToast(mContext,"数据已同步");
                                    }else {
                                        ToastUtils.showShortToast(mContext,"上传失败");
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
                        throwable.printStackTrace();
                    }
                });
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
                            Log.e("xxx",string);
                            if (Utils.isGoodJson(string)){
                                Gson gson = new Gson();
                                UploadIvEntry uploadIvEntry = gson.fromJson(string, UploadIvEntry.class);
                                if (uploadIvEntry.getCode()==Constants.SUCCESS_CODE){
                                    Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                                    mImagePathList.add(uploadIvEntry.getData());
                                    commonAdapter.notifyDataSetChanged();
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


    private MyLocationListener myLocationListener;
    private void initLocation() {
        //定位客户端的设置
        //定位初始化
        if (!isFinishing()){
            commonLoadDialog = DialogBuild.getBuild().createCommonLoadDialog(this,"定位中...");
        }
        if (null==mLocationClient){
            mLocationClient = new LocationClient(this);
        }
//通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(10000);

//设置locationClientOption
        mLocationClient.setLocOption(option);
//注册LocationListener监听器
        if (null==myLocationListener){
            myLocationListener = new MyLocationListener();
        }
        mLocationClient.registerLocationListener(myLocationListener);
//开启地图定位图层
        if (mLocationClient.isStarted()){
            mLocationClient.stop();
        }
        mLocationClient.start();
    }
}
