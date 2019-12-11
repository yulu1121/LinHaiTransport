package com.anshi.linhaitransport.view.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseActivity;
import com.anshi.linhaitransport.selfview.ZoomImageView;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.utils.glide.GlideApp;

/**
 *
 * Created by yulu on 2017/11/29.
 */

public class ImageActivity extends BaseActivity {
    private ZoomImageView imageView;
    private String picPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity_main);
        picPath = getIntent().getStringExtra("picPath");
        initView();
    }

    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText("图片预览");
        imageView = (ZoomImageView) findViewById(R.id.find_image);
        if (!TextUtils.isEmpty(picPath)){

                GlideApp.with(this).load(picPath).centerInside().into(imageView);

        }else {
            Toast.makeText(this, "图片路径错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        StatusBarUtils.setStatusTextColor(true,this);
    }


}
