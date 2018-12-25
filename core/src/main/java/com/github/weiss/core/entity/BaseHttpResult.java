package com.github.weiss.core.entity;

import com.github.weiss.core.api.NullableResult;

/**
 * Created by Weiss on 2017/1/11.
 */

public abstract class BaseHttpResult<T> extends Entity {

    public abstract boolean isSuccess();

    public abstract boolean isTokenInvalid();

    public abstract boolean isShowToast();

    public abstract String getCode();

    public abstract String getMsg();

    public abstract T getData();

    public NullableResult<T> nullable() {
        return new NullableResult<T>(getData());
    }
}
