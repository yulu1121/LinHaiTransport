package com.anshi.linhaitransport.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.view.login.LoginActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


public class DialogBuild {

    private static DialogBuild build;

    public interface OnConfirmListener {
        void onConfirm(Dialog dialog, boolean isConfirm);
    }

    private DialogBuild() {

    }

    public static DialogBuild getBuild() {
        if (build == null) {
            build = new DialogBuild();
        }
        return build;
    }


    public AlertDialog createPhoneDialog(final Context context, final String phone){
        return new AlertDialog.Builder(context)
                .setTitle("呼叫客服")
                .setCancelable(true)
                .setMessage("确定要拨打:"+phone)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utils.dialPhoneNumber(context,phone);
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
    }

    public KProgressHUD createCommonProgressDialog(Activity activity, String message){
        return KProgressHUD.create(activity)
                .setCancellable(true)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setMaxProgress(100)
                .setLabel(message)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

    }
    public KProgressHUD createCommonLoadDialog(Activity activity, String message){
        return KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }
    public AlertDialog createOneButtonNoCancelDialog(final Activity context, final String title, String message){
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        SharedPreferenceUtils.clear(context);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        context.finishAffinity();

                    }
                }).create();
    }



    public AlertDialog createLogOutDialog(final Context context){
        return new AlertDialog.Builder(context)
                .setTitle("退出")
                .setCancelable(false)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferenceUtils.clear(context);
                        dialogInterface.dismiss();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finishAffinity();
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
    }



    public AlertDialog createOneButtonNoFinishDialog(final Activity context, final String title, String message){
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
    }
    public AlertDialog createOneButtonFinishDialog(final Activity context, final String title, String message){
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.finish();
                        dialogInterface.dismiss();
                    }
                }).create();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showPermissionRationale(Activity activity, final PermissionToken token, String message) {
        StringBuilder messages= new StringBuilder();
        if (message.contains("CAMERA")){
            messages.append("需要拍照权限\t");
        }else if (message.contains("READ_EXTERNAL_STORAGE")||message.contains("WRITE_EXTERNAL_STORAGE")){
            messages.append("需要读写SD卡权限\t");
        }else if (message.contains("LOCATION")){
            messages.append("需要定位权限\t");
        }
        new AlertDialog.Builder(activity).setTitle("权限提醒")
                .setMessage(messages)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.cancelPermissionRequest();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.continuePermissionRequest();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .show();
    }

    public MultiplePermissionsListener createPermissionDialog(Context context, String title, String message){
        return DialogOnAnyDeniedMultiplePermissionsListener.Builder
                .withContext(context)
                .withTitle(title)
                .withMessage(message)
                .withButtonText(android.R.string.ok)
                .withIcon(R.mipmap.ic_launcher)
                .build();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showPermissionDeny(final Activity activity, String permission) {
        StringBuilder message = new StringBuilder();
        if (permission.contains("CAMERA")){
            message.append("拍照权限被拒绝");
        }else if (permission.contains("READ_EXTERNAL_STORAGE")||permission.contains("WRITE_EXTERNAL_STORAGE")){
            message.append("读写SD卡权限被拒绝");
        }else if (permission.contains("LOCATION")){
            message.append("定位权限被拒绝");
        }
        new AlertDialog.Builder(activity).setTitle("权限提醒")
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.toSelfSetting(activity);
                        dialog.dismiss();

                    }
                })
                .show();
    }


}