package com.github.weiss.core.bus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 用于管理Rxjava相关代码的生命周期处理
 */
public class RxManager {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();// 管理订阅者者


    public void add(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void clear() {
        compositeDisposable.clear();// 取消订阅
    }
}
