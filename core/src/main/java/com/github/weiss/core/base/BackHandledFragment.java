package com.github.weiss.core.base;

import android.support.v4.view.ViewPager;

import com.github.weiss.core.utils.helper.BackHandlerHelper;
import com.github.weiss.core.utils.helper.FragmentBackHandler;

public abstract class BackHandledFragment extends BaseRxFragment implements FragmentBackHandler {

    public BackHandledFragment() {
    }

    @Override
    public final boolean onBackPressed() {
        return interceptBackPressed()
                || (getBackHandleViewPager() == null
                ? BackHandlerHelper.handleBackPress(this)
                : BackHandlerHelper.handleBackPress(getBackHandleViewPager()));
    }

    public boolean interceptBackPressed() {
        return false;
    }

    /**
     * 2.1 版本已经不在需要单独对ViewPager处理
     *
     * @deprecated
     */
    @Deprecated
    public ViewPager getBackHandleViewPager() {
        return null;
    }
}
