package com.github.weiss.core;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.weiss.core.util.ToastUtil;

import butterknife.ButterKnife;

/**
 * Created by Weiss on 2017/1/10.
 */

public abstract class BaseActivity extends AppCompatActivity{
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getLayoutId());
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        initView();
    }

    public void ShowToast(String msg){
        ToastUtil.show(msg);
    }

    public void ShowProgress(String msg){
        progressDialog.setTitle(msg);
        progressDialog.show();
    }

    //token失效处理
    public void tokenInvalid( ){

    }

    protected abstract boolean isLogin();

    protected abstract int getLayoutId();

    protected abstract void initView();
}
