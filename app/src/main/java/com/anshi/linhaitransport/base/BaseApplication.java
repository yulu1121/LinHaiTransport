package com.anshi.linhaitransport.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.SharedPreferenceUtils;
import com.anshi.linhaitransport.utils.cookie.OkHttpUtils;
import com.anshi.linhaitransport.utils.glide.TestImageLoader;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.previewlibrary.ZoomMediaLoader;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tencent.smtt.sdk.QbSdk;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseApplication extends Application {
    public static BaseApplication instances;
    private  Retrofit mAppRetrofit;
    public Retrofit getAppRetrofit(){
        return mAppRetrofit;
    }

    //http://apicloud.mob.com/v1/weather/query?key=appkey&city=通州&province=北京
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorBlue, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        OkHttpUtils.initOkHttp(this);
        //ijk内核，默认模式
        //http://47.128.149.8:8080/
        mAppRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.COMMON_URL_HEADER)
                .client(OkHttpUtils.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        ZoomMediaLoader.getInstance().init(new TestImageLoader());
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                SharedPreferenceUtils.saveBoolean(getApplicationContext(),"hasLoad",b);
            }
        });
    }

    public static BaseApplication getInstances(){
        return instances;
    }


}
