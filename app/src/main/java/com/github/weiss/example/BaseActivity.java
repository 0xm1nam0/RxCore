package com.github.weiss.example;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.weiss.core.base.BaseRxActivity;
import com.github.weiss.core.entity.BaseHttpResult;

/**
 * Created by Weiss on 2017/1/17.
 */

public abstract class BaseActivity extends BaseRxActivity {

    //token失效处理
    public void tokenInvalid() {
        LogUtils.d("tokenInvalid");
        App.userManager.logout();
        ToastUtils.showShort("请安全退出，重新登录账号");

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
