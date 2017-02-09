package com.github.weiss.example;

import com.github.weiss.core.BaseApp;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.ButterKnife;

/**
 * Created by Weiss on 2017/1/10.
 */

public class App extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(true);
        CrashReport.initCrashReport(getApplicationContext(), "7f7492a815", false);
    }
}
