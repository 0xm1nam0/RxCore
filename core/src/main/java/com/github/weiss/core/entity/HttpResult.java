package com.github.weiss.core.entity;

/**
 * Created by Weiss on 2017/1/11.
 */

public class HttpResult<T> extends BaseEntity.BaseBean {
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

    public boolean hasMore() {
        return hasmore;
    }

}
