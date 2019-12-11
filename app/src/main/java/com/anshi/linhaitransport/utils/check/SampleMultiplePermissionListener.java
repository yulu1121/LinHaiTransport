package com.anshi.linhaitransport.utils.check;

/*
 *
 * Created by yulu on 2018/6/23.
 */

import android.app.Activity;

import com.anshi.linhaitransport.utils.DialogBuild;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SampleMultiplePermissionListener implements MultiplePermissionsListener {

    private Activity activity;
    private PermissionsChecked permissionsChecked;
    public SampleMultiplePermissionListener(Activity activity) {
        this.activity = activity;
    }
    public SampleMultiplePermissionListener(Activity activity,PermissionsChecked permissionsChecked) {
        this.activity = activity;
        this.permissionsChecked = permissionsChecked;
    }



    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        if (report.areAllPermissionsGranted()){
            if (null!=permissionsChecked){
                permissionsChecked.permissionsChecked(true);
            }
        }else {
            for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()) {
                DialogBuild.getBuild().showPermissionDeny(activity,response.getPermissionName());
            }
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                   PermissionToken token) {
        DialogBuild.getBuild().showPermissionRationale(activity,token,permissions.toString());
    }

    public interface PermissionsChecked{
        void permissionsChecked(boolean check);
    }
}