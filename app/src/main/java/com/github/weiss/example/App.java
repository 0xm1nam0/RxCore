package com.github.weiss.example;

import com.github.weiss.core.BaseApp;
import com.squareup.leakcanary.LeakCanary;

import butterknife.ButterKnife;

/**
 * Created by Weiss on 2017/1/10.
 */

public class App extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(true);
//        CrashReport.initCrashReport(getApplicationContext(), "", false);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
