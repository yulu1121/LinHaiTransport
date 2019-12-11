package com.anshi.linhaitransport.view.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
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
import com.anshi.linhaitransport.utils.StatusBarUtils;
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
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
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
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainMapActivity extends BaseActivity implements OnGetSuggestionResultListener,OnGetPoiSearchResultListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private static final String CUSTOM_FILE_NAME_GRAY = "custom_map_config.json";
    private EditText mRoadEt;
    private SuggestionSearch mSearch;
    private String city;
    private PoiSearch mPoiSearch;
    private LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        initView();
        initLocation();
        initPopuData();
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSearch = SuggestionSearch.newInstance();
        mSearch.setOnGetSuggestionResultListener(this);
        addEventListener();
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

    @SuppressLint("InflateParams")
    private void initView(){
        TextView title = findViewById(R.id.title_tv);
        title.setText("凌海市地图");
        mRoadEt = findViewById(R.id.road_et);
        mMapView = findViewById(R.id.mapview);
        mLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.baidu_map_infowindow,null);
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
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMarkerClick(final Marker marker) {

                EditText mText = mLinearLayout.findViewById(R.id.et_name);
                EditText phone = mLinearLayout.findViewById(R.id.et_person);
                EditText textView = mLinearLayout.findViewById(R.id.et_xunlu);
                Button uploadBtn = mLinearLayout.findViewById(R.id.upload_btn);
                uploadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBaiduMap.hideInfoWindow();
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
            mBaiduMap.clear();
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
            mBaiduMap.clear();
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
            MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(poiDetailInfoList.get(0).getLocation());
            mBaiduMap.setMapStatus(status);//直接到中间
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

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
                    .accuracy(location.getRadius())
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
