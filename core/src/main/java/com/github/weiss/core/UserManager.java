package com.github.weiss.core;

import android.text.TextUtils;

import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.SPUtils;
import com.google.gson.Gson;

/**
 * author weiss
 * email kleinminamo@gmail.com
 * created 2018/1/4.
 */

public class UserManager<T> {
    private static final String TAG = "UserManager";
    public static String FILE = "user_info";
    public static String KEY = "user_message";
    public static String KEY_TOKEN = "user_token";
    public static String IS_LOGIN = "user_is_login";
    private T mModel;

    public UserManager(Class<T> tClass) {
        String user_message = SPUtils.getString(FILE, KEY);
        if(!TextUtils.isEmpty(user_message)) {
            this.mModel = (new Gson()).fromJson(user_message, tClass);
        }

    }

    public boolean isLogin() {
        return SPUtils.getBoolean(FILE, IS_LOGIN);
    }

    public T getUserModel() {
        return this.mModel;
    }

    public void login(T t) {
        if(t == null) {
            LogUtils.e("UserManager", "Login Model is Null");
        } else if(SPUtils.getBoolean(FILE, IS_LOGIN)) {
            LogUtils.e("UserManager", "Can't Login Again");
        } else {
            this.mModel = t;
            String user_message = (new Gson()).toJson(t);
            SPUtils.put(FILE, KEY, user_message);
            SPUtils.put(FILE, IS_LOGIN, true);
        }
    }

    public void logout() {
        SPUtils.put(FILE, IS_LOGIN, false);
    }

    public String getToken() {
        return SPUtils.getString(FILE, KEY_TOKEN);
    }

    public void setToken(String token) {
        SPUtils.put(FILE, KEY_TOKEN, token);
    }
}
