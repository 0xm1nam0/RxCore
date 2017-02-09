package com.github.weiss.example.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.weiss.core.entity.BaseListEntity;
import com.github.weiss.core.entity.HttpResult;
import com.github.weiss.core.utils.helper.RxException;
import com.github.weiss.example.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class PtrRecyclerView<T extends BaseListEntity> extends LinearLayout {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout ptrFrame;

    private Context context;
    private MultiTypeAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private BaseListEntity model;
    private int page = 1;
    private Map<String, String> param = new HashMap<>();

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

    public PtrRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public PtrRecyclerView setParam(String key, String value) {
        this.param.put(key, value);
        return this;
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
        ButterKnife.bind(this, layout);
//        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
//        ptrFrame = (PtrFrameLayout) layout.findViewById(R.id.store_house_ptr_frame);
        initView(context);
    }

    private void initView(Context context) {
        ptrFrame.setLastUpdateTimeRelateObject(this);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
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
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerView.setLayoutManager(layout);
    }


    public void setAdapter(MultiTypeAdapter adapter, BaseListEntity cls) {
        this.adapter = adapter;
        initAdapter();
        recyclerView.setAdapter(adapter);
        model=cls;
        ptrFrame.autoRefresh();
    }

    private void initAdapter() {

    }

    public void onLoadMoreRequested() {
        ptrFrame.setEnabled(false);
        page++;
        request();
    }

    private void request() {
        if (model == null) {
            Log.e("model", "null");
            return;
        }
        model.setParam(param);
        compositeDisposable.add(model.getPage(page)
                .doAfterTerminate(() -> {
                    if (page == 1) {
                        ptrFrame.refreshComplete();
                    } else {

                    }
                })
                .subscribe(new Consumer<HttpResult<List<T>>>() {
                               @Override
                               public void accept(HttpResult<List<T>> httpListResult) throws Exception {
                                   adapter.setItems(httpListResult.results);
                                   adapter.notifyDataSetChanged();
                               }
                           },
                        new RxException<>(e -> e.printStackTrace()))
        );
    }
}
