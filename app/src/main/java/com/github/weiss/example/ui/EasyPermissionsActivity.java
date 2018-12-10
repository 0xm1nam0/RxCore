package com.github.weiss.example.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.weiss.example.BaseActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class EasyPermissionsActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    private static final String TAG = "EasyPermissionsActivity";
    private static final String[] PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };

    private static final int RC_PERM = 124;
    private boolean isFirst = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            //因为要通过一个Fragment来弹出弹出框，所以activity这里的onResume执行了两次，这里进行判断
            isFirst = false;
            permissionsTask();

        }
    }

    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(this, PERMISSION);
    }

    private boolean hasStoragePermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    @AfterPermissionGranted(RC_PERM)
    public void permissionsTask() {
        if (hasPermissions()) {
            startMainActivity();
            // Have permissions, do the thing!
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    "需要获取以下权限，才能正常使用APP",
                    RC_PERM,
                    PERMISSION);
        }
    }

    public abstract void startMainActivity();

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //接受的权限List
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    //拒绝的权限List
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //这个方法有个前提是，用户点击了“不再询问”后，才判断权限没有被获取到
            new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置界面以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
            isFirst = true;
        } else if (!hasPermissions()) {
            //这里响应的是除了AppSettingsDialog这个弹出框，剩下的两个弹出框被拒绝或者取消的效果
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (!hasPermissions()) {
                finish();
            }
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d(TAG, "onRationaleDenied:" + requestCode);
    }
}
