package com.github.weiss.core.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.drakeet.multitype.ItemViewBinder;
import com.github.weiss.core.R;
import com.github.weiss.core.entity.LoadMore;

/**
 * author weiss
 * email kleinminamo@gmail.com
 * created 2018/8/28.
 */
public class LoadMoreViewBinder extends ItemViewBinder<LoadMore, LoadMoreViewBinder.ViewHolder> {

    View.OnClickListener onLoadMoreClickListener;

    public void setOnLoadMoreClickListener(View.OnClickListener onLoadMoreClickListener) {
        this.onLoadMoreClickListener = onLoadMoreClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_load_more, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LoadMore loadMore) {
        holder.onLoadMoreClickListener = onLoadMoreClickListener;
        if (loadMore.status == LoadMore.STATUS_LOADMORE) {
            holder.moreProgressBar.setVisibility(View.VISIBLE);
            holder.loadMoreButton.setVisibility(View.GONE);
            holder.moreTextView.setText("正在加载更多...");
        } else if (loadMore.status == LoadMore.STATUS_ERROR) {
            holder.loadMoreButton.setVisibility(View.VISIBLE);
        } else if (loadMore.status == LoadMore.STATUS_COMPLETED) {
            holder.loadMoreButton.setVisibility(View.GONE);
            holder.moreProgressBar.setVisibility(View.GONE);
            holder.moreTextView.setText("----数据已经加载完毕---");
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button loadMoreButton;
        TextView moreTextView;
        ProgressBar moreProgressBar;

        View.OnClickListener onLoadMoreClickListener;

        ViewHolder(View itemView) {
            super(itemView);
            loadMoreButton = itemView.findViewById(R.id.loadMore_button);
            moreTextView = itemView.findViewById(R.id.more_textView);
            moreProgressBar = itemView.findViewById(R.id.more_progressBar);
            loadMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreButton.setVisibility(View.GONE);
                    if (onLoadMoreClickListener != null) {
                        onLoadMoreClickListener.onClick(v);
                    }
                }
            });
        }
    }

}
