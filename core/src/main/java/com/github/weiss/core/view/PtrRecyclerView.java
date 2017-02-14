package com.github.weiss.core.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.weiss.core.BaseCoreActivity;
import com.github.weiss.core.BaseRxActivity;
import com.github.weiss.core.R;
import com.github.weiss.core.entity.BaseListEntity;
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

public class PtrRecyclerView extends LinearLayout {

    //    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    //    @BindView(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout ptrFrame;

    private Context context;
    private MultiTypeAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<BaseListEntity> listResult = new ArrayList<>();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        ptrFrame = (PtrClassicFrameLayout) layout.findViewById(R.id.store_house_ptr_frame);
//        ButterKnife.bind(this, layout);
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
        recyclerView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
                page++;
                request();
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }

        });
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerView.setLayoutManager(layout);
    }


    public void setAdapter(MultiTypeAdapter adapter, BaseListEntity model) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
        this.model = model;
        ptrFrame.autoRefresh();
    }

    private void request() {
        if (model == null) {
            Log.e("model", "null");
            return;
        }
        if (page != 1) {
            //演示等待对话框用法，加载更多不推荐这样使用
            ((BaseCoreActivity) getContext()).showProgress("正在加载更多");
        }
        model.setParam(param);
        compositeDisposable.add(model.getPage(page)
                .compose(((BaseRxActivity) getContext()).handleResult())
                .doAfterTerminate(() -> {
                    if (page == 1) {
                        ptrFrame.refreshComplete();
                    } else {
                        ((BaseCoreActivity) getContext()).dismissProgress();
                    }
                })
                .subscribe(new Consumer<List<BaseListEntity>>() {
                               @Override
                               public void accept(List<BaseListEntity> results) throws Exception {
                                   if (page == 1) {
                                       listResult.clear();
                                       listResult = results;
                                   } else {
                                       listResult.addAll(results);
                                   }
                                   adapter.setItems(listResult);
                                   adapter.notifyDataSetChanged();
                               }
                           },
                        new RxException<>(e -> e.printStackTrace()))
        );
    }
}
