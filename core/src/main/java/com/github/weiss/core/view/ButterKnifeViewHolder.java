package com.github.weiss.core.view;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by weiss on 2017/4/27.
 */

public class ButterKnifeViewHolder extends RecyclerView.ViewHolder {

    protected ButterKnifeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
