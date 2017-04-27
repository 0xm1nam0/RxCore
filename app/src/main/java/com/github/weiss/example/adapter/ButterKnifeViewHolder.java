package com.github.weiss.example.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by weiss on 2017/4/27.
 */

public class ButterKnifeViewHolder extends RecyclerView.ViewHolder {

    ButterKnifeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
