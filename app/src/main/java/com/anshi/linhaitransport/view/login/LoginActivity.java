package com.anshi.linhaitransport.view.login;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseActivity;
import com.anshi.linhaitransport.entry.LoginEntry;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.DialogBuild;
import com.anshi.linhaitransport.utils.SharedPreferenceUtils;
import com.anshi.linhaitransport.utils.Utils;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    private EditText mUserNameEt;
    private EditText mPassWordEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTranslucentForCoordinatorLayout(this,0);


    }



    private void initView() {
        mUserNameEt = findViewById(R.id.userName_et);
        mPassWordEt = findViewById(R.id.password_et);
    }

    public void login(View view) {
        if (TextUtils.isEmpty(mUserNameEt.getText())){
            Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mPassWordEt.getText())){
            Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        loginApp(mUserNameEt.getText().toString(),mPassWordEt.getText().toString());
    }
    private KProgressHUD commonLoadDialog;
    private void loginApp(String userName,String password){
        if (!isFinishing()){
            commonLoadDialog = DialogBuild.getBuild().createCommonLoadDialog(this,"正在加载中");
        }
        mService.loginApp(userName,password)
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
                                Gson gson = new Gson();
                                LoginEntry loginEntry = gson.fromJson(string, LoginEntry.class);
                                if (loginEntry.getCode()== Constants.SUCCESS_CODE){
                                    SharedPreferenceUtils.saveBoolean(mContext,"autoLogin",true);
                                    SharedPreferenceUtils.saveInt(mContext,"userId",loginEntry.getData().getUserId());
                                    SharedPreferenceUtils.saveInt(mContext,"deptId",loginEntry.getData().getDeptId());
                                    SharedPreferenceUtils.saveInt(mContext,"areaId",loginEntry.getData().getAreaId());
                                    SharedPreferenceUtils.saveString(mContext,"deptName",loginEntry.getData().getDept().getDeptName());
                                    Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    },2000);
                                }else {
                                    Toast.makeText(mContext, loginEntry.getMsg(), Toast.LENGTH_SHORT).show();
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
