package com.github.weiss.core.entity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by Weiss on 2017/1/21.
 */

public abstract class BaseListEntity<T> extends BaseEntity.BaseBean implements BaseEntity.IListBean {

    @Override
    public void setParam(Map<String, String> param) {
        this.param=param;
    }

    @Override
    public abstract Flowable<HttpResult<List<T>>> getPage(int page);
}
