package com.github.weiss.core.entity;

import java.util.Map;

import io.reactivex.Observable;

/**
 * author weiss
 * email kleinminamo@gmail.com
 * created 2018/2/27.
 */
interface IListBean {
    //获取第几页
    Observable getPage(int page);

    //设置网络请求参数
    void setParam(Map<String, String> param);
}
