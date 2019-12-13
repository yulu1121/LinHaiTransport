package com.anshi.linhaitransport.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseActivity;
import com.anshi.linhaitransport.utils.DialogBuild;
import com.anshi.linhaitransport.utils.SharedPreferenceUtils;
import com.anshi.linhaitransport.utils.Utils;
import com.anshi.linhaitransport.utils.WeakHandler;
import com.anshi.linhaitransport.utils.check.SampleMultiplePermissionListener;
import com.anshi.linhaitransport.view.home.PlatFrag;
import com.anshi.linhaitransport.view.home.child.CommonChildFrag;
import com.anshi.linhaitransport.view.login.LoginActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;

public class MainActivity extends BaseActivity{
    private  boolean isExit = false;
    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    isExit = false;
                    break;
            }
            return true;
        }
    });
    private RadioGroup mTabGroup;//菜单Group
    private RadioButton mHomeRb;//主页
    private RadioButton mPlatRb;//我的
    private int mainTextColor;
    private int normalTextColor;
    private Fragment lastFragment;
    private FragmentManager mFragmentMgr;
    private CommonChildFrag mHomeFrag;
    private PlatFrag mPlatFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SampleMultiplePermissionListener multiplePermissionListener = new SampleMultiplePermissionListener(this);
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new CompositeMultiplePermissionsListener(multiplePermissionListener, DialogBuild.getBuild().createPermissionDialog(this,"权限提醒","请给予拍照和定位的权限")))
                .check();
        setContentView(R.layout.activity_main);
//        StatusBarUtil.setLightMode(this);
//        StatusBarUtil.setTranslucentForCoordinatorLayout(this,0);
        initView();
        loadFragments();
        initClick();
        addEventListener();
    }



    private void initView() {
        mTabGroup = findViewById(R.id.main_radio_group);
        mHomeRb = findViewById(R.id.home_rb);
        mPlatRb = findViewById(R.id.plat_rb);
        mainTextColor = Utils.getMainTextColor(this);
        normalTextColor = Utils.getMainThemeColor(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }



    private void addEventListener(){
        mTabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home_rb:
                        setFragment(mHomeFrag);
                        setColor();
                        mHomeRb.setTextColor(mainTextColor);
                        break;
                    case R.id.plat_rb:
                        if (SharedPreferenceUtils.getBoolean(mContext,"autoLogin")){
                            setFragment(mPlatFrag);
                            setColor();
                            mPlatRb.setTextColor(mainTextColor);
                        }else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivityForResult(intent,11);
                            mTabGroup.check(R.id.home_rb);
                        }
                        break;

                }
            }
        });
    }


    /**
     * 退出应用程序
     */
    public void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(mContext, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finishAffinity();
        }
    }
    //加载fragments
    private void loadFragments() {
        mFragmentMgr = getSupportFragmentManager();
        mHomeFrag = CommonChildFrag.getInstance("jsj");
        mPlatFrag = PlatFrag.getInstance();
        lastFragment = mHomeFrag;
    }
    //初始化选中界面
    private void initClick(){
        mTabGroup.check(R.id.home_rb);
        mHomeRb.setTextColor(mainTextColor);
        mFragmentMgr.beginTransaction().add(R.id.main_frame_layout,mHomeFrag).commit();
    }
    //设置当前fragment
    private void setFragment(Fragment fragment){
        if(!fragment.isAdded()){
            mFragmentMgr.beginTransaction().hide(lastFragment).add(R.id.main_frame_layout,fragment).commitAllowingStateLoss();
        }else{
            mFragmentMgr.beginTransaction().hide(lastFragment).show(fragment).commitAllowingStateLoss();
        }
        lastFragment =  fragment;
    }
    //还原默认字体颜色
    private void setColor(){
        mHomeRb.setTextColor(normalTextColor);
        mPlatRb.setTextColor(normalTextColor);
    }

}
