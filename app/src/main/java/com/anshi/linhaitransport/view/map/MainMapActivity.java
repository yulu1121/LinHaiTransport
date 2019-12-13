package com.anshi.linhaitransport.view.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseActivity;
import com.anshi.linhaitransport.entry.MapMarkerEntry;
import com.anshi.linhaitransport.entry.MarkerInfoUtil;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.DialogBuild;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.utils.ToastUtils;
import com.anshi.linhaitransport.utils.Utils;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainMapActivity extends BaseActivity implements OnGetSuggestionResultListener,OnGetPoiSearchResultListener,OnGetDistricSearchResultListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private static final String CUSTOM_FILE_NAME_GRAY = "custom_map_config.json";
    private EditText mRoadEt;
    private SuggestionSearch mSearch;
    private String city;
    private PoiSearch mPoiSearch;
    private String mRoad;
    private DistrictSearch mDistrictSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(this);
        initView();
        initLocation();
        initPopuData();
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSearch = SuggestionSearch.newInstance();
        mSearch.setOnGetSuggestionResultListener(this);
        addEventListener();
        getAllMarkerList();
    }
    private PopupWindow popupWindow;
    private List<SuggestionResult.SuggestionInfo> mGradeList = new ArrayList<>();
    private CommonAdapter<SuggestionResult.SuggestionInfo> commonAdapter;
    private void initPopuData(){
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.popu_recycler, null);
        RecyclerView mGradeRecyclerView = view.findViewById(R.id.grade_recycler);
        mGradeRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        commonAdapter = new CommonAdapter<SuggestionResult.SuggestionInfo>(mContext,R.layout.item_grade,mGradeList) {
            @Override
            protected void convert(ViewHolder holder, final SuggestionResult.SuggestionInfo gradeListBean, int position) {
                    TextView textView = holder.getView(R.id.grade_item_tv);
                    textView.setText(gradeListBean.key);
                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (popupWindow.isShowing()){
                                popupWindow.dismiss();
                            }
                            mRoad = gradeListBean.key;
                            Utils.hideSoftKeyboard(MainMapActivity.this);
                            mPoiSearch.searchPoiDetail(new PoiDetailSearchOption()
                                    .poiUids(gradeListBean.uid));
                        }
                    });
            }
        };
        mGradeRecyclerView.setAdapter(commonAdapter);
        popupWindow =  new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(null);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
    }

    private void drawLine(List<LatLng> points){
        //设置折线的属性
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(10)
                .color(0xAAFF0000)
                .points(points);
//在地图上绘制折线
//mPloyline 折线对象
        mBaiduMap.addOverlay(mOverlayOptions);
    }


    private void getAllMarkerList(){
        mDistrictSearch.searchDistrict(new DistrictSearchOption()
                .cityName("凌海"));
        mService.getAllRoadInfo()
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
                                MapMarkerEntry mapMarkerEntry = gson.fromJson(string, MapMarkerEntry.class);
                                if (mapMarkerEntry.getCode()== Constants.SUCCESS_CODE){
                                    if (mapMarkerEntry.getData()!=null&&mapMarkerEntry.getData().size()>0){
                                        setMarkerInfo(mapMarkerEntry);
                                    }
                                }else {
                                    ToastUtils.showShortToast(mContext,mapMarkerEntry.getMsg());
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


    private void setMarkerInfo(MapMarkerEntry markerInfo) {
         List<MarkerInfoUtil> options = new ArrayList<>();
        List<MapMarkerEntry.DataBean> data = markerInfo.getData();
        for (int i = 0; i < data.size(); i++) {
            MapMarkerEntry.DataBean dataBean = data.get(i);
            MarkerInfoUtil markerInfoUtil = new MarkerInfoUtil();
            markerInfoUtil.setmMarkerId(dataBean.getId());
            markerInfoUtil.setBulidTime(dataBean.getBuildTime());
            markerInfoUtil.setLatitude(Double.parseDouble(dataBean.getLatitude()));
            markerInfoUtil.setLongitude(Double.parseDouble(dataBean.getLongitude()));
            markerInfoUtil.setName(dataBean.getRoadName());
            markerInfoUtil.setRoadManager(dataBean.getRoadManager());
            markerInfoUtil.setRoadDeal(dataBean.getRoadInspector());
            markerInfoUtil.setRoadYangHu(dataBean.getRoadMaintenance());
            options.add(markerInfoUtil);
        }
        LatLng latLng ;
        OverlayOptions option;
        List<OverlayOptions> optionsList = new ArrayList<>();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.pg_location);
        for(MarkerInfoUtil info:options){
            //获取经纬度
            latLng = new LatLng(info.getLatitude(),info.getLongitude());
            //设置marker
            Bundle bundle = new Bundle();
            //info必须实现序列化接口
            bundle.putSerializable("address",info);
            option = new MarkerOptions()
                    .position(latLng)//设置位置
                    .extraInfo(bundle)
                    .icon(bitmapDescriptor);
            optionsList.add(option);
            //添加marker
            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
        }
        mBaiduMap.addOverlays(optionsList);
    }

    private KProgressHUD kProgressHUD;
    //String id,String lat,String lng,String roadName,String roadManager,String roadInspector,String roadMaintenance,String remarks,String buildTime
    private void saveMarkerToServer(String id,String lat,String lng,String roadName,String roadManager,String roadInspector,String roadMaintenance,String buildTime){
        if (!isFinishing()){
            kProgressHUD = DialogBuild.getBuild().createCommonLoadDialog(this,"正在加载");
        }
        HashMap<String,String> hashMap = new HashMap<>();

            if (!TextUtils.isEmpty(id)){
                hashMap.put("id",id);
            }
            hashMap.put("lat",lat);
            hashMap.put("lng",lng);
            hashMap.put("roanName",roadName);
            hashMap.put("roadManager",roadManager);
            hashMap.put("roadInspector",roadInspector);
            hashMap.put("roadMaintenance",roadMaintenance);
            hashMap.put("buildTime",buildTime);
        mService.saveRoadApp(hashMap)
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
                                try {
                                    JSONObject jsonObject1 = new JSONObject(string);
                                    int code = jsonObject1.getInt("code");
                                    String msg = jsonObject1.getString("msg");
                                    if (code==Constants.SUCCESS_CODE){
                                        ToastUtils.showShortToast(mContext,"提交成功");
                                        mBaiduMap.clear();
                                        getAllMarkerList();
                                    }else {
                                        ToastUtils.showShortToast(mContext,msg);
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
                        if (null!=kProgressHUD){
                            kProgressHUD.dismiss();
                        }
                        throwable.printStackTrace();
                    }
                });
    }





    @SuppressLint("InflateParams")
    private void initView(){
        TextView title = findViewById(R.id.title_tv);
        title.setText("凌海市地图");
        mRoadEt = findViewById(R.id.road_et);
        mMapView = findViewById(R.id.mapview);
        // 不显示百度地图Logo
        mMapView.removeViewAt(1);
        String customStyleFilePath = getCustomStyleFilePath(this, CUSTOM_FILE_NAME_GRAY);
        // 设置个性化地图样式文件的路径和加载方式
        mMapView.setMapCustomStylePath(customStyleFilePath);
        // 动态设置个性化地图样式是否生效
        mMapView.setMapCustomStyleEnable(true);
        mBaiduMap = mMapView.getMap();
        MapStatus mMapStatus = new MapStatus.Builder().zoom(16.0f).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.setMyLocationEnabled(true);
        //setMarkerInfo();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMarkerClick(final Marker marker) {
                LinearLayout mLinearLayout = (LinearLayout) LayoutInflater.from(MainMapActivity.this).inflate(R.layout.baidu_map_infowindow,null);
                TextView roadName = mLinearLayout.findViewById(R.id.road_name);
                final EditText bulidTime = mLinearLayout.findViewById(R.id.et_name);
                final EditText mManagerEt = mLinearLayout.findViewById(R.id.et_person);
                final EditText xunluEt = mLinearLayout.findViewById(R.id.et_xunlu);
                final EditText yanghuEt = mLinearLayout.findViewById(R.id.et_yanghu);
                final MarkerInfoUtil address = (MarkerInfoUtil) marker.getExtraInfo().getSerializable("address");
                Button uploadBtn = mLinearLayout.findViewById(R.id.upload_btn);
                if (null!=address){
                    uploadBtn.setText("修改");
                    roadName.setText(address.getName());
                    bulidTime.setText(address.getBulidTime());
                    mManagerEt.setText(address.getRoadManager());
                    xunluEt.setText(address.getRoadDeal());
                    yanghuEt.setText(address.getRoadYangHu());
                    mRoad = address.getName();
                }else{
                    roadName.setText(mRoad);
                    uploadBtn.setText("提交");
                }
                roadName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBaiduMap.hideInfoWindow();
                    }
                });
                uploadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBaiduMap.hideInfoWindow();
                        if (null!=address){
                            saveMarkerToServer(address.getmMarkerId(),String.valueOf(address.getLatitude()),String.valueOf(address.getLongitude()),
                                    mRoad,mManagerEt.getText().toString(),xunluEt.getText().toString(),yanghuEt.getText().toString(),bulidTime.getText().toString());
                        }else {
                            saveMarkerToServer(null,String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude),
                                    mRoad,mManagerEt.getText().toString(),xunluEt.getText().toString(),yanghuEt.getText().toString(),bulidTime.getText().toString());
                        }
                    }
                });
                InfoWindow window = new InfoWindow(mLinearLayout,marker.getPosition(),-47);
                marker.showInfoWindow(window);
                return true;
            }
        });
    }

    private void addEventListener(){
        mRoadEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                mSearch.requestSuggestion(new SuggestionSearchOption()
                .city("凌海").keyword(string).citylimit(true));
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        StatusBarUtils.setStatusTextColor(true,this);
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
        mSearch.destroy();
        mDistrictSearch.destroy();
    }


    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult.getAllSuggestions()!=null&&suggestionResult.getAllSuggestions().size()>0){
            if (!popupWindow.isShowing()){
                popupWindow.showAsDropDown(mRoadEt);
            }
            mGradeList.clear();
            mGradeList.addAll(suggestionResult.getAllSuggestions());
            commonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult==null||poiResult.getAllPoi()==null){
            return;
        }
        List<PoiInfo> allPoi = poiResult.getAllPoi();
        if (allPoi.size()>0){
            List<LatLng> mList = new ArrayList<>();
            for (int i = 0; i <allPoi.size() ; i++) {
                    mList.add(allPoi.get(i).getLocation());
            }
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.pg_location);
//构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(allPoi.get(0).location)
                    .icon(bitmap);
//在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
            MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(allPoi.get(0).location);
            mBaiduMap.setMapStatus(status);//直接到中

            if (mList.size()>1){
                drawLine(mList);
            }
        }

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
        if (poiDetailInfoList!=null&&poiDetailInfoList.size()>0){
            for (int i = 0; i < poiDetailInfoList.size(); i++) {
                LatLng location = poiDetailInfoList.get(i).getLocation();
                LatLng point = new LatLng(location.latitude, location.longitude);
//构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.pg_location);
//构建MarkerOption，用于在地图上添加Marker

                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
//在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);
            }
            Log.e("xxx",poiDetailInfoList.get(0).getLocation()+"");
            MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(poiDetailInfoList.get(0).getLocation());
            mBaiduMap.setMapStatus(status);//直接到中间

        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        if (null != districtResult) {
            //获取边界坐标点，并展示
            if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
                List<List<LatLng>> polyLines = districtResult.getPolylines();
                if (polyLines == null) {
                    return;
                }
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (List<LatLng> polyline : polyLines) {
                    OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                            .points(polyline).dottedLine(true).color(Color.BLUE);
                    mBaiduMap.addOverlay(ooPolyline11);
                    for (LatLng latLng : polyline) {
                        builder.include(latLng);
                    }
                }
                mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        .newLatLngBounds(builder.build()));
            }
        }
    }

    //自定义的定位监听
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //将获取的location信息给百度map
            if (location == null || mMapView == null){
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            city = location.getCity();
            LatLng ll = new LatLng(41.16638,121.362395);
            MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.setMapStatus(status);//直接到中间
        }
    }

    private void initLocation() {
        //定位客户端的设置
        //定位初始化
        mLocationClient = new LocationClient(this);

//通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);

//设置locationClientOption
        mLocationClient.setLocOption(option);

//注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
//开启地图定位图层
        mLocationClient.start();
    }
}
