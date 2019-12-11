package com.anshi.linhaitransport.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.anshi.linhaitransport.net.AppHttpService;

/**
 *
 * Created by yulu on 2018/3/13.
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected AppHttpService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = BaseActivity.this;
        mService = BaseApplication.getInstances().getAppRetrofit().create(AppHttpService.class);
    }


    public void back(View view){
        finish();
    }
}
