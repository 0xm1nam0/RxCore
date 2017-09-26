package com.github.weiss.example;

import com.github.weiss.core.BaseRxActivity;
import com.github.weiss.core.entity.HttpResult;

/**
 * Created by Weiss on 2017/1/17.
 */

public abstract class BaseActivity extends BaseRxActivity {

    //token失效处理
    public void tokenInvalid() {

    }

    //是否登录
    protected boolean isLogin() {
        return false;
    }

    //是否HandleResult
    protected boolean needHandleResult(HttpResult result) {
        if (result.isTokenInvalid()) {
            tokenInvalid();
            return true;
        } else if (!isLogin()) {
            //执行登录操作
            return true;
        } else {
            return false;
        }
    }
}
