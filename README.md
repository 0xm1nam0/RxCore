
简书地址
[RxJava2 + Retrofit2 优雅简洁封装](http://www.jianshu.com/p/4005bc4a20f2)

![RxJava2 + Retrofit2.jpg](https://github.com/WeissWill/Core/raw/master/screenshot/core.jpg)

###封装方案
######1、封装 Rx 线程相关
**RxSchedulersHelper：**
```
/**
 * Created by weiss on 17/1/16.
 */
public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> io_main() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
```
**封装后：**
```
Api.getInstance().movieService
                .getGankData("Android",1)
                .compose(RxSchedulers.io_main());
```
######2、封装 处理服务器返回数据，管理 RxJava生命周期
**HttpResult：**

```
/**
 * Created by Weiss on 2017/1/11.
 */

public class HttpResult<T> implements Serializable {
    public String code;
    public String msg;
    public boolean hasmore;
    public T data;

    public static String SUCCESS = "000";
    public static String SIGN_OUT = "101";//token验证失败
    public static String SHOWTOAST = "102";//显示Toast

    public boolean isSuccess() {
        return SUCCESS.equals(code);
    }

    public boolean isTokenInvalid() {
        return SIGN_OUT.equals(code);
    }

    public boolean isShowToast() {
        return SHOWTOAST.equals(code);
    }

    public boolean hasMore() {
        return hasmore;
    }
}
```
**BaseRxActivity：**
```

/**
 * 管理RxJava生命周期，避免内存泄漏
 * RxJava处理服务器返回
 *
 * Created by Weiss on 2016/12/23.
 */

public abstract class BaseRxActivity extends BaseActivity {

    private CompositeDisposable disposables2Stop;// 管理Stop取消订阅者者
    private CompositeDisposable disposables2Destroy;// 管理Destroy取消订阅者者

    protected abstract int getLayoutId();

    protected abstract void initView();

    /**
     * Rx优雅处理服务器返回
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<HttpResult<T>, T> handleResult() {
        return upstream ->{
                return upstream.flatMap(result -> {
                            if (result.isSuccess()) {
                                return createData(result.data);
                            } else if (result.isTokenInvalid()) {
                                //处理token失效，tokenInvalid方法在BaseActivity 实现
                                 tokenInvalid();
                            } else {
                                return Observable.error(new Exception(result.msg));
                            }
                            return Observable.empty();
                        }

                );
        };
    }

    private <T> Observable<T> createData(final T t) {
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (disposables2Destroy != null) {
            throw new IllegalStateException("onCreate called multiple times");
        }
        disposables2Destroy = new CompositeDisposable();
    }

    public void onStart() {
        super.onStart();
        if (disposables2Stop != null) {
            throw new IllegalStateException("onStart called multiple times");
        }
        disposables2Stop = new CompositeDisposable();
    }

    public void onStop() {
        super.onStop();
        if (disposables2Stop == null) {
            throw new IllegalStateException("onStop called multiple times or onStart not called");
        }
        disposables2Stop.dispose();
        disposables2Stop = null;
    }

    public void onDestroy() {
        super.onDestroy();
        if (disposables2Destroy == null) {
            throw new IllegalStateException(
                    "onDestroy called multiple times or onCreate not called");
        }
        disposables2Destroy.dispose();
        disposables2Destroy = null;
    }
}
```
**封装后 (BaseRxActivity的子类使用)：**
```
addRxDestroy(Api.getInstance().movieService
                .getGankData("Android",1)
                .compose(RxSchedulers.io_main())
                .compose(handleResult())
                .省略
);
```
handleResult为什么不新建一个类处理呢？因为很多异常处理需要context对象，或者和BaseActivity有千丝万缕的联系，BaseRxActivity继承BaseActivity可以很简洁优雅处理各种异常。比如token失效，是需要跳转到登录页面的。在新建一个类中，不能持有context对象，只能使用Application的Context，同时方便与Activity通信交互满足各种需求。BaseRxActivity还可以管理RxJava生命周期。
######3、异常处理
```

/**
 * Created by Weiss on 2017/2/9.
 */

public class RxException<T extends Throwable> implements Consumer<T> {

    private static final String TAG = "RxException";

    private static final String SOCKETTIMEOUTEXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试";
    private static final String CONNECTEXCEPTION = "网络连接异常，请检查您的网络状态";
    private static final String UNKNOWNHOSTEXCEPTION = "网络异常，请检查您的网络状态";

    private Consumer<? super Throwable> onError;
    public RxException(Consumer<? super Throwable> onError) {
        this.onError=onError;
    }

    /**
     * Consume the given value.
     *
     * @param t the value
     * @throws Exception on error
     */
    @Override
    public void accept(T t) throws Exception {
        if (t instanceof SocketTimeoutException) {
            Log.e(TAG, "onError: SocketTimeoutException----" + SOCKETTIMEOUTEXCEPTION);
            ToastUtils.show(SOCKETTIMEOUTEXCEPTION);
            onError.accept(new Throwable(SOCKETTIMEOUTEXCEPTION));
        } else if (t instanceof ConnectException) {
            Log.e(TAG, "onError: ConnectException-----" + CONNECTEXCEPTION);
            ToastUtils.show(CONNECTEXCEPTION);
            onError.accept(new Throwable(CONNECTEXCEPTION));
        } else if (t instanceof UnknownHostException) {
            Log.e(TAG, "onError: UnknownHostException-----" + UNKNOWNHOSTEXCEPTION);
            ToastUtils.show(UNKNOWNHOSTEXCEPTION);
            onError.accept(new Throwable(UNKNOWNHOSTEXCEPTION));
        } else {
            Log.e(TAG, "onError:----" + t.getMessage());
            onError.accept(t);
        }
    }
}
```

**封装后 (BaseRxActivity的子类使用)：**
```
addRxDestroy(Api.getInstance().movieService
                .getGankData("Android",1)
                .compose(RxSchedulers.io_main())
                .compose(handleResult())
                .subscribe(httpResult -> adapter.setItems(httpResult.data),
                                new RxException<>(e ->e.printStackTrace()))
);
```

######4、多页请求封装
**实体类：**
```
/**
 * Created by Weiss on 2017/1/20.
 */

public class Gank extends BaseListEntity {
    @Override
    public Flowable<HttpResult<List<Gank>>> getPage(int page) {
        return GankApi.getInstance().service.getGankData(param.get("gank"), page)
                .compose(RxSchedulers.io_main());
    }

}
```
实体类继承 BaseListEntity，实现getPage方法即可。

**UI视图：**
```
public class MainActivityFragment extends BaseRxFragment {
    @BindView(R.id.baseRecyclerView)
    PtrRecyclerView ptrRecyclerView;

    private MultiTypeAdapter adapter;

    public MainActivityFragment() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        ptrRecyclerView.setParam("gank","Android");
        adapter = new MultiTypeAdapter();
        adapter.register(Gank.class,new GankViewProvider());
        ptrRecyclerView.setAdapter(adapter,new Gank());
    }
}

```
只需要写一个适配器和实体类，轻松实现多页请求，PtrRecyclerView下拉刷新和上拉加载自动获取数据。
PtrRecyclerView目前只是简单实现下拉刷新和上拉加载，有空会完善，当然例子也会完善。
###后记
还可以封装网络加载对话框，这个看个人喜好，同样以上封装同样可以看个人喜好和项目需求自由组装。

######相关链接
[RxJava 2.0有什么不同(译)](http://blog.csdn.net/qq_35064774/article/details/53045298)

[探索专为 Android 而设计的 RxJava 2](https://realm.io/cn/news/gotocph-jake-wharton-exploring-rxjava2-android/)
