package com.github.weiss.example.ui;

import android.support.v7.widget.StaggeredGridLayoutManager;

import com.github.weiss.core.BaseRxFragment;
import com.github.weiss.example.R;
import com.github.weiss.example.adapter.GankViewProvider;
import com.github.weiss.example.entity.Gank;
import com.github.weiss.core.view.PtrRecyclerView;

import butterknife.BindView;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseRxFragment {
    @BindView(R.id.baseRecyclerView)
    PtrRecyclerView ptrRecyclerView;

    private MultiTypeAdapter adapter;
    private String type;

    public MainActivityFragment() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        type = "Android";
        ptrRecyclerView.setParam("gank", type);
        ptrRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new MultiTypeAdapter();
        adapter.register(Gank.class, new GankViewProvider(type));
        ptrRecyclerView.setAdapter(adapter, new Gank());
    }
}
