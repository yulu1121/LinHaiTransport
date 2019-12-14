package com.anshi.linhaitransport.view.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.utils.SharedPreferenceUtils;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.view.casedeal.CaseDealActivity;
import com.anshi.linhaitransport.view.casedeal.ManagerDealActivity;
import com.anshi.linhaitransport.view.endcase.EndCaseActivity;
import com.anshi.linhaitransport.view.filemanager.FileManagerActivity;
import com.anshi.linhaitransport.view.map.MainMapActivity;
import com.anshi.linhaitransport.view.roadstructure.RoadStructureActivity;
import com.anshi.linhaitransport.view.tourmanager.TourManagerActivity;
import com.tencent.bugly.beta.Beta;

public class PlatFrag extends Fragment implements View.OnClickListener {
    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }
    public static PlatFrag getInstance(){
        return new PlatFrag();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_plat,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView mainMap = view.findViewById(R.id.main_map_tv);
        mainMap.setOnClickListener(this);
        TextView fileManagerTv = view.findViewById(R.id.file_manager_tv);
        fileManagerTv.setOnClickListener(this);
        TextView tourTv = view.findViewById(R.id.tour_tv);
        String deptName = SharedPreferenceUtils.getString(mContext, "deptName");
        if (deptName.contains("巡路员")){
            tourTv.setText("巡查");
        }else if (deptName.contains("养护员")){
            tourTv.setText("养护");
        }
        view.findViewById(R.id.tour_tv).setOnClickListener(this);
        view.findViewById(R.id.case_deal_tv).setOnClickListener(this);
        //view.findViewById(R.id.curing_tv).setOnClickListener(this);
        view.findViewById(R.id.end_case_tv).setOnClickListener(this);
        view.findViewById(R.id.out_login_tv).setOnClickListener(this);
        view.findViewById(R.id.road_manager_tv).setOnClickListener(this);
        view.findViewById(R.id.version_tv).setOnClickListener(this);
        completeView(view);

    }

    private void completeView(View view){
        String deptName = SharedPreferenceUtils.getString(mContext, "deptName");
        if (deptName.contains("巡路员")||deptName.contains("养护员")){
            view.findViewById(R.id.main_map_tv).setVisibility(View.GONE);
            view.findViewById(R.id.file_manager_tv).setVisibility(View.GONE);
            view.findViewById(R.id.end_case_tv).setVisibility(View.GONE);
            if (deptName.contains("养护员")){
                view.findViewById(R.id.case_deal_tv).setVisibility(View.GONE);
            }else {
                view.findViewById(R.id.case_deal_tv).setVisibility(View.VISIBLE);
            }
        }else {
            view.findViewById(R.id.tour_tv).setVisibility(View.GONE);
            view.findViewById(R.id.curing_tv).setVisibility(View.GONE);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.colorBlue);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (this!=null&&!hidden){
            StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.colorBlue);
            //StatusBarUtils.setStatusTextColor(false,getActivity());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_map_tv:
                Intent intent = new Intent(mContext, MainMapActivity.class);
                startActivity(intent);
                break;
            case R.id.road_manager_tv:
                Intent intent6 = new Intent(mContext, RoadStructureActivity.class);
                startActivity(intent6);
                break;
            case R.id.file_manager_tv:
                Intent intent1 = new Intent(mContext, FileManagerActivity.class);
                startActivity(intent1);
                break;
            case R.id.tour_tv:
                Intent intent2 = new Intent(mContext, TourManagerActivity.class);
                startActivity(intent2);
                break;
//            case R.id.curing_tv:
//                Intent intent3 = new Intent(mContext, CuringActivity.class);
//                startActivity(intent3);
//                break;
            case R.id.case_deal_tv:
                if (SharedPreferenceUtils.getString(mContext,"deptName").contains("巡路员")){
                    Intent intent4 = new Intent(mContext, CaseDealActivity.class);
                    startActivity(intent4);
                }else {
                    Intent intent4 = new Intent(mContext, ManagerDealActivity.class);
                    startActivity(intent4);
                }
                break;
            case R.id.end_case_tv:
                Intent intent5 = new Intent(mContext, EndCaseActivity.class);
                startActivity(intent5);
                break;
            case R.id.out_login_tv:
                createOutLogin();
                break;
            case R.id.version_tv:
                Beta.checkUpgrade(true,false);
                break;
        }
    }
    private void createOutLogin() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("提醒")
                .setMessage("退出登录")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferenceUtils.clear(mContext);
                        if (null!=getActivity()){
                            getActivity().finishAffinity();
                        }
                    }
                })
                .create();
        if (getActivity() != null && !getActivity().isFinishing()) {
            alertDialog.show();
        }
    }
}
