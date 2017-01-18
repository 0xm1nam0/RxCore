package com.github.weiss.core;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.github.weiss.core.util.SharedPreferencesUtils;

/**
 * Created by Weiss on 2017/1/10.
 */

public class App extends Application {

    private static App app;

    public static Context getAppContext() {
        return app;
    }

    public static Resources getAppResources() {
        return app.getResources();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesUtils.init(this);
    }
}
