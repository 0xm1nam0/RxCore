package com.github.weiss.example;

import com.github.weiss.core.BaseRxActivity;

/**
 * Created by Weiss on 2017/1/17.
 */

public class BaseAppActivity extends BaseRxActivity {
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected boolean isLogin() {
        return false;
    }

    @Override
    protected void initView() {

    }
}
