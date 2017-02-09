package com.github.weiss.example.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.weiss.core.utils.ImageLoaderUtil;
import com.github.weiss.example.R;
import com.github.weiss.example.entity.Gank;
import com.github.weiss.example.ui.PictureActivity;
import com.github.weiss.example.view.RatioImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Weiss on 2017/2/8.
 */
public class GankViewProvider
        extends ItemViewProvider<Gank, GankViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_gank, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Gank gank) {
        holder.title.setText(gank.desc);
        holder.image.setOriginalSize(50, 50);
        ImageLoaderUtil.loadGifImg(holder.image, gank.imageUrl);
//        ImageUtil.loadAdapterImg(image,item.getImageUrl(),holder.itemView);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPictureActivity(gank, v);
            }
        });
    }

    private void startPictureActivity(Gank gank, View transitView) {
        Intent intent = PictureActivity.newIntent(transitView.getContext(), gank.imageUrl, gank.desc);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                (Activity) transitView.getContext(), transitView, PictureActivity.TRANSIT_PIC);
        try {
            ActivityCompat.startActivity((Activity) transitView.getContext(), intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            transitView.getContext().startActivity(intent);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.image)
        RatioImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}