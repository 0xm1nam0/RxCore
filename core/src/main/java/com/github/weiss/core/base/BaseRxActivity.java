package com.github.weiss.core.base;

import android.os.Bundle;

import com.github.weiss.core.api.NullableResult;
import com.github.weiss.core.entity.BaseHttpResult;
import com.github.weiss.core.utils.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * 管理RxJava生命周期，避免内存泄漏
 * RxJava处理服务器返回
 * <p>
 * Created by Weiss on 2016/12/23.
 */

public abstract class BaseRxActivity extends BaseCoreActivity {

    protected CompositeDisposable disposables2Stop;// 管理Stop取消订阅者者
    protected CompositeDisposable disposables2Destroy;// 管理Destroy取消订阅者者

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract boolean needHandleResult(BaseHttpResult result);

    /**
     * Rx优雅处理服务器返回
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<BaseHttpResult<T>, NullableResult<T>> handleResult() {
        return upstream -> upstream
                .flatMap((Function<BaseHttpResult<T>, ObservableSource<NullableResult<T>>>) result -> {
                            if (result.isSuccess()) {
                                return createData(result.nullable());
                            } else if (result.isShowToast()) {
                                ToastUtils.show(result.getMsg());
                            } else if (!needHandleResult(result)) {
                                return Observable.error(new Exception(result.getMsg()));
                            }else {
                                return Observable.error(new Exception(result.getMsg()));
                            }
                            return Observable.empty();
                        }
                );
    }

    protected <T> Observable<NullableResult<T>> createData(final NullableResult<T> t) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(t);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    public boolean addRxStop(Disposable disposable) {
        if (disposables2Stop == null) {
            throw new IllegalStateException(
                    "addUtilStop should be called between onStart and onStop");
        }
        disposables2Stop.add(disposable);
        return true;
    }

    public boolean addRxDestroy(Disposable disposable) {
        if (disposables2Destroy == null) {
            throw new IllegalStateException(
                    "addUtilDestroy should be called between onCreate and onDestroy");
        }
        disposables2Destroy.add(disposable);
        return true;
    }

    public void remove(Disposable disposable) {
        if (disposables2Stop == null && disposables2Destroy == null) {
            throw new IllegalStateException("remove should not be called after onDestroy");
        }
        if (disposables2Stop != null) {
            disposables2Stop.remove(disposable);
        }
        if (disposables2Destroy != null) {
            disposables2Destroy.remove(disposable);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        if (disposables2Destroy != null) {
            throw new IllegalStateException("onCreate called multiple times");
        }
        disposables2Destroy = new CompositeDisposable();
        super.onCreate(savedInstanceState);
    }

    protected void onStart() {
        if (disposables2Stop != null) {
            throw new IllegalStateException("onStart called multiple times");
        }
        disposables2Stop = new CompositeDisposable();
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
        if (disposables2Stop == null) {
            throw new IllegalStateException("onStop called multiple times or onStart not called");
        }
        disposables2Stop.dispose();
        disposables2Stop = null;
    }

    protected void onDestroy() {
        super.onDestroy();
        if (disposables2Destroy == null) {
            throw new IllegalStateException(
                    "onDestroy called multiple times or onCreate not called");
        }
        disposables2Destroy.dispose();
        disposables2Destroy = null;
    }
}
