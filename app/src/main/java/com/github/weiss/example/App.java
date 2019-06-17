package com.github.weiss.example;

import com.github.weiss.core.base.BaseApp;
import com.github.weiss.core.UserManager;
import com.github.weiss.core.crash.CaocConfig;
import com.github.weiss.example.entity.UserModel;
import com.github.weiss.example.ui.MainActivity;
import com.squareup.leakcanary.LeakCanary;

import butterknife.ButterKnife;

/**
 * Created by Weiss on 2017/1/10.
 */

public class App extends BaseApp {

    public static UserManager<UserModel> userManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(true);
//        CrashReport.initCrashReport(getApplicationContext(), "", false);
        userManager = new UserManager<>(UserModel.class);
        initCrash();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(MainActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }
}
