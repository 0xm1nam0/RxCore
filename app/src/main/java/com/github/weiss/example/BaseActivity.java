package com.github.weiss.example;

import com.github.weiss.core.BaseRxActivity;
import com.github.weiss.core.entity.BaseHttpResult;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.ToastUtils;

/**
 * Created by Weiss on 2017/1/17.
 */

public abstract class BaseActivity extends BaseRxActivity {

    //token失效处理
    public void tokenInvalid() {
        LogUtils.d("tokenInvalid");
        App.userManager.logout();
        ToastUtils.show("请安全退出，重新登录账号");

    }

    //是否登录
    protected boolean isLogin() {
        return false;
    }

    //是否HandleResult
    protected boolean needHandleResult(BaseHttpResult result) {
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
