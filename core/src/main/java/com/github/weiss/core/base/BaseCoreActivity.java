package com.github.weiss.core.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.blankj.utilcode.util.ToastUtils;

import butterknife.ButterKnife;

/**
 * Created by Weiss on 2017/1/10.
 */

public abstract class BaseCoreActivity extends AppCompatActivity implements View.OnClickListener {
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(savedInstanceState!=null){
//            String name=savedInstanceState.getString("recover");
//        }
        //隐藏标题栏以及状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        *标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题*
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getLayoutId() > 0) {
            setContentView(this.getLayoutId());
        }
//        SystemBarHelper.immersiveStatusBar(this);
        ButterKnife.bind(this);
        this.preInit(this.getIntent());
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        initView();
        initData();
/*        if(!handleLogin()) {
        }*/
    }

    //是否登录
    protected boolean needLogin() {
        return false;
    }

    public void showToast(String msg) {
        ToastUtils.showShort(msg);
    }

    public void showProgress(String msg) {
        if(isFinishing()) return;
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            super.onBackPressed();
        }

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void preInit(Intent intent) {

    }

    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
