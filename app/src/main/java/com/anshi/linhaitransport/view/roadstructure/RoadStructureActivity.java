package com.anshi.linhaitransport.view.roadstructure;

import android.os.Bundle;
import android.widget.TextView;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseActivity;
import com.anshi.linhaitransport.selfview.ZoomImageView;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.utils.glide.GlideApp;

public class RoadStructureActivity extends BaseActivity {
    private ZoomImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_structure);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorBlue);
    }

    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        mImageView = findViewById(R.id.zoom_iv);
        GlideApp.with(this).load(R.drawable.pg_road_structure).centerCrop().into(mImageView);
    }
}
