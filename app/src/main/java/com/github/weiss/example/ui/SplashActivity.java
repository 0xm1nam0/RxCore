package com.github.weiss.example.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.flyco.systembar.SystemBarHelper;
import com.github.weiss.example.BaseActivity;
import com.github.weiss.example.R;

/**
 * author weiss
 * email kleinminamo@gmail.com
 * created 2017/12/15.
 */
public class SplashActivity extends EasyPermissionsActivity {

    @Override
    protected int getLayoutId() {
        //隐藏标题栏以及状态栏
/*        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        *//**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**//*
        requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        SystemBarHelper.immersiveStatusBar(this);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(0);
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getHome();
            super.handleMessage(msg);
        }
    };

    public void getHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void startMainActivity() {
        handler.sendEmptyMessageDelayed(0, 1500);
    }
}
