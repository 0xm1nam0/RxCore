package com.github.weiss.example;

import com.github.weiss.core.entity.BaseHttpResult;

/**
 * Created by Weiss on 2017/1/11.
 */

public class HttpResult<T> extends BaseHttpResult<T> {
    public String code;
    public String msg;
    public boolean hasmore;
    public T results;
    public boolean error;

    public static String SUCCESS = "000";
    public static String SIGN_OUT = "101";//token验证失败
    public static String SHOW_TOAST = "102";//显示Toast

//    public boolean isSuccess() {
//        return SUCCESS.equals(code);
//    }

    public boolean isSuccess() {
        return !error;
    }

    public boolean isTokenInvalid() {
        return SIGN_OUT.equals(code);
    }

    public boolean isShowToast() {
        return SHOW_TOAST.equals(code);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public T getData() {
        return results;
    }

    public boolean hasMore() {
        return hasmore;
    }

}
