package com.github.weiss.core.utils.helper;

import com.blankj.utilcode.util.ToastUtils;
import com.github.weiss.core.api.NullableResult;
import com.github.weiss.core.entity.BaseHttpResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class RxHandle {
    /**
     * Rx优雅处理服务器返回
     *
     * @param <T>
     * @return
     */
    public static  <T> ObservableTransformer<BaseHttpResult<T>, NullableResult<T>> handleResult() {
        return upstream -> upstream
                .flatMap((Function<BaseHttpResult<T>, ObservableSource<NullableResult<T>>>) result -> {
                            if (result.isSuccess()) {
                                return createData(result.nullable());
                            } else if (result.isShowToast()) {
                                ToastUtils.showShort(result.getMsg());
                            }else {
                                return Observable.error(new Exception(result.getMsg()));
                            }
                            return Observable.empty();
                        }
                );
    }

    protected static <T> Observable<NullableResult<T>> createData(final NullableResult<T> t) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(t);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
