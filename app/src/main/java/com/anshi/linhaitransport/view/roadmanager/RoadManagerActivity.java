package com.anshi.linhaitransport.view.roadmanager;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseActivity;
import com.anshi.linhaitransport.selfview.ProgressWebView;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.StatusBarUtils;

public class RoadManagerActivity extends BaseActivity {
    private ProgressWebView mWebView;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_manger);
        id = getIntent().getIntExtra("id", 0);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorBlue);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        String title = getIntent().getStringExtra("title");
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText(title);
        mWebView = findViewById(R.id.web_view);
        WebSettings webSetting =mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);//允许js调用
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        webSetting.setAllowFileAccess(true);//在File域下，能够执行任意的JavaScript代码，同源策略跨域访问能够对私有目录文件进行访问等
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//控制页面的布局(使所有列的宽度不超过屏幕宽度)
        webSetting.setSupportZoom(true);//支持页面缩放
        webSetting.setBuiltInZoomControls(true);//进行控制缩放
        webSetting.setAllowContentAccess(true);//是否允许在WebView中访问内容URL（Content Url），默认允许
        webSetting.setUseWideViewPort(true);//设置缩放密度
        webSetting.setSupportMultipleWindows(false);//设置WebView是否支持多窗口,如果为true需要实现onCreateWindow(WebView, boolean, boolean, Message)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //两者都可以
            webSetting.setMixedContentMode(webSetting.getMixedContentMode());//设置安全的来源
        }
        webSetting.setAppCacheEnabled(true);//设置应用缓存
        webSetting.setDomStorageEnabled(true);//DOM存储API是否可用
        webSetting.setLoadWithOverviewMode(true);//是否允许WebView度超出以概览的方式载入页面，
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);//设置应用缓存内容的最大值
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);//设置是否支持插件
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);//重写使用缓存的方式
        webSetting.setAllowUniversalAccessFromFileURLs(true);//是否允许运行在一个file schema URL环境下的JavaScript访问来自其他任何来源的内容
        webSetting.setAllowFileAccessFromFileURLs(true);//是否允许运行在一个URL环境
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //                super.onPageFinished(view, url);
                //重新为WebView设置高度
            }
        });
        mWebView.loadUrl(Constants.WEB_URL+String.valueOf(id));
    }



    @Override
    public void onBackPressed() {
        if (null!=mWebView&&mWebView.canGoBack()){
            mWebView.goBack();

            return;
        }
//        //先返回正常状态
        super.onBackPressed();
    }


}
