package com.github.weiss.core.api;

import android.support.annotation.Nullable;

import java.util.NoSuchElementException;

public class NullableResult<T> {

    private final T nullableResult; // 返回结果
    public NullableResult(@Nullable T nullableResult) {
        this.nullableResult = nullableResult;
    }

    public boolean isEmpty() {
        return this.nullableResult == null;
    }

    // 获取不能为null的返回结果，如果为null，直接抛异常，这个异常最终可以在走向RxJava的onError()
    public T get() {
        if (nullableResult == null) {
            throw new NoSuchElementException("No value present");
        }
        return nullableResult;
    }

    // 获取可以为null的返回结果
    public T getNullable() {
        return nullableResult;
    }
}
