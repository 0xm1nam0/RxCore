package com.github.weiss.core.util.helper;


import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by weiss on 17/1/16.
 */
public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> io_main() {
        return tObservable ->
                tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
