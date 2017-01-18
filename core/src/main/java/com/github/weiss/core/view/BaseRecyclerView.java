package com.github.weiss.core.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.weiss.core.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Weiss on 2017/1/17.
 */

public class BaseRecyclerView extends LinearLayout {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.store_house_ptr_frame)
    PtrFrameLayout storeHousePtrFrame;

    private Context context;

    public BaseRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
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
        initView(context);
    }

    private void initView(Context context) {
        swiperefresh.setColorSchemeResources(android.R.color.holo_blue_bright);
        swiperefresh.setEnabled(isRefreshable);
        swiperefresh.setOnRefreshListener(() -> reFetch());
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mCommAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            protected int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter() != null
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == recyclerView.getAdapter()
                        .getItemCount() && mCommAdapter.isHasMore)
                    fetch();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int arg0, int arg1) {
                super.onScrolled(recyclerView, arg0, arg1);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        mRxManage.on(C.EVENT_DEL_ITEM, (arg0) -> mCommAdapter.removeItem((Integer) arg0));
        mRxManage.on(C.EVENT_UPDATE_ITEM, (arg0) -> mCommAdapter.upDateItem(((UpDateData) arg0).i, ((UpDateData) arg0).oj));
        ll_emptyview.setOnClickListener((view -> reFetch()));
    }
}
