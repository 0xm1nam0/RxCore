package com.github.weiss.core.entity;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by Weiss on 2017/1/21.
 */

public abstract class HttpListResult<T> extends HttpResult<T> implements BaseEntity.IListBean {

    public Map<String, String> param;
    @Override
    public void setParam(Map<String, String> param) {
        this.param=param;
    }

    @Override
    public abstract Flowable getPage(int page);
}
