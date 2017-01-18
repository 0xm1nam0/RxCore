package com.github.weiss.core.entity;

/**
 * Created by Weiss on 2017/1/11.
 */

public class HttpResult<T> extends BaseEntity.BaseBean {
    public String resultCode;
    public String resultMsg;
    public boolean hasmore;
    public T result;

    public static String SUCCESS = "000";
    public static String SIGN_OUT = "101";//token验证失败

    public boolean isSuccess() {
        return SUCCESS.equals(resultCode);
    }


    public boolean isTokenInvalid() {
        return SIGN_OUT.equals(resultCode);
    }

    public boolean hasMore() {
        return hasmore;
    }

    public String getResultcode() {
        return resultCode;
    }

    public void setResultcode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResult_msg() {
        return resultMsg;
    }

    public void setResult_msg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
