package com.github.weiss.core.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Weiss on 2017/1/10.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public View rootView;
    public LayoutInflater inflater;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.inflater = inflater;
        if (rootView == null) {
            rootView = inflater.inflate(this.getLayoutId(), container, false);
            ButterKnife.bind(this, rootView);
            parseArguments(getArguments());
            initView();
            initData();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    public View findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    @Override
    public void onClick(View v) {

    }

    protected void parseArguments(Bundle bundle) {

    }

    protected void initData() {

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

}
