package com.anshi.linhaitransport.utils.check;

/**
 *
 * Created by yulu on 2018/6/23.
 */

import android.app.Activity;

import com.anshi.linhaitransport.utils.DialogBuild;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class SamplePermissionListener implements PermissionListener {

    private final Activity activity;
    private PermissionChecked permissionChecked;
    public SamplePermissionListener(Activity activity,PermissionChecked permissionChecked) {
        this.activity = activity;
        this.permissionChecked = permissionChecked;
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
                permissionChecked.permissionChecked(true);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        DialogBuild.getBuild().showPermissionDeny(activity,response.getPermissionName());
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                   PermissionToken token) {
        DialogBuild.getBuild().showPermissionRationale(activity,token,permission.toString());
    }

    public interface PermissionChecked{
        void permissionChecked(boolean check);
    }
}