package com.github.weiss.core.entity;

import java.io.Serializable;
import java.util.Map;

import io.reactivex.Observable;


public interface BaseEntity {
    class BaseBean implements Serializable {
    }

    interface IListBean {
        //获取第几页
        Observable getPage(int page);
        //设置网络请求参数
        void setParam(Map<String, String> param);
    }

    abstract class ListBean extends BaseBean implements IListBean {

        public Map<String, String> param;
        @Override
        public void setParam(Map<String, String> param) {
            this.param=param;
        }
    }
}
