package com.github.weiss.core.entity;

import java.util.List;

import io.reactivex.Observable;

/**
 * author weiss
 * email kleinminamo@gmail.com
 * created 2018/8/28.
 */
public class LoadMore extends ListEntity {

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_LOADMORE = 2;
    public static final int STATUS_COMPLETED = 3;

    public int status;

    @Override
    public Observable<BaseHttpResult<List>> getPage(int page) {
        return null;
    }
}