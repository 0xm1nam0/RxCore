package com.github.weiss.core.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.weiss.core.BaseRxActivity;
import com.github.weiss.core.R;
import com.github.weiss.core.api.NullableResult;
import com.github.weiss.core.entity.ListEntity;
import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.helper.RxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Weiss on 2017/1/17.
 */

public class PtrRecyclerView extends LinearLayout implements LoadMoreDelegate.LoadMoreSubject {

    //    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    //    @BindView(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout ptrFrame;

    private LoadMoreDelegate loadMoreDelegate;

    private Context context;
    private MultiTypeAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<ListEntity> listResult = new ArrayList<>();
    private ListEntity model;
    public int page = 0;
    public final static int PAGEBEGIN = 0;
    private Map<String, String> param = new HashMap<>();
    private RxListener rxListener;
    private boolean isRequest = false;

    public PtrRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public PtrRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PtrRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PtrRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public PtrRecyclerView setParam(String key, String value) {
        this.param.put(key, value);
        return this;
    }

    public void setRxListener(RxListener rxListener) {
        this.rxListener = rxListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        compositeDisposable.clear();
    }

    public void init(Context context) {
        this.context = context;
        View layout = LayoutInflater.from(context).inflate(
                R.layout.recyclerview_base, null);
        layout.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addView(layout);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        ptrFrame = (PtrClassicFrameLayout) layout.findViewById(R.id.store_house_ptr_frame);
        loadMoreDelegate = new LoadMoreDelegate(this);
//        ButterKnife.bind(this, layout);
        initView(context);
    }

    private void initView(Context context) {
        ptrFrame.setLastUpdateTimeRelateObject(this);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = PAGEBEGIN;
                request();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
//                page++;
//                request();
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }

        });
        loadMoreDelegate.attach(recyclerView);
    }

    public void setPullToRefresh(boolean pullToRefresh) {
        ptrFrame.setPullToRefresh(pullToRefresh);
    }

    public void refresh() {
        page = PAGEBEGIN;
        request();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerView.setLayoutManager(layout);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        recyclerView.addOnScrollListener(onScrollListener);
    }

    public void setAdapter(MultiTypeAdapter adapter, ListEntity model) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
        this.model = model;
        ptrFrame.autoRefresh();
    }

    private void request() {
        isRequest = true;
        if (model == null) {
            LogUtils.e("model", "null");
            return;
        }
        if (page != PAGEBEGIN) {
            //演示等待对话框用法，加载更多不推荐这样使用
//            BaseRxActivity().showProgress("正在加载更多");
        }
        model.setParam(param);
        compositeDisposable.add(model.getPage(page)
                .compose(BaseRxActivity().handleResult())
                .doAfterTerminate(() -> {
                    if (page == PAGEBEGIN) {
                        ptrFrame.refreshComplete();
                    } else {
                        BaseRxActivity().dismissProgress();
                    }
                    if (rxListener != null) {
                        rxListener.onCompleted();
                    }
                    isRequest = false;
                })
                .subscribe(new Consumer<NullableResult<List<ListEntity>>>() {
                               @Override
                               public void accept(NullableResult<List<ListEntity>> results) throws Exception {
                                   if (page == PAGEBEGIN) {
                                       listResult.clear();
                                       listResult = results.get();
                                   } else {
                                       listResult.addAll(results.get());
                                   }
                                   if (rxListener != null) {
                                       if (CollectionUtils.isEmpty(listResult)) {
                                           rxListener.onEmpty();
                                       } else {
                                           rxListener.onSuccess();
                                       }
                                   }
                                   if (CollectionUtils.isEmpty(results.get())) {
                                       page--;
                                   } else {
                                       adapter.setItems(listResult);
                                       adapter.notifyDataSetChanged();
                                   }
                               }
                           },
                        new RxException<>(e -> {
                            if(page > PAGEBEGIN) {
                                page--;
                            }
                            e.printStackTrace();
                            if (rxListener != null) {
                                rxListener.onError(e.getMessage());
                            }
                        }))
        );
    }

    public BaseRxActivity BaseRxActivity() {
        return (BaseRxActivity) context;
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    protected boolean onInterceptLoadMore() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void onLoadMore() {
        if (!onInterceptLoadMore() && !isRequest) {
            page++;
            request();
        }
    }

    public interface RxListener {
        void onSuccess();

        void onError(String Throwable);

        void onEmpty();

        void onCompleted();
    }
}
