package com.github.weiss.example.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.weiss.core.utils.ImageLoaderUtil;
import com.github.weiss.example.R;
import com.github.weiss.example.entity.Gank;
import com.github.weiss.example.ui.GankDetailsActivity;
import com.github.weiss.example.ui.PictureActivity;
import com.github.weiss.example.view.RatioImageView;

import butterknife.BindView;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Weiss on 2017/2/8.
 */
public class GankViewProvider extends ItemViewProvider<Gank, GankViewProvider.ViewHolder> {

    private String type;

    public GankViewProvider(String type) {
        this.type = type;
    }

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
        holder.image.setOnClickListener(view -> startPictureActivity(gank, view));
        holder.itemView.setOnClickListener(view -> startWebActivity(gank, holder.itemView));
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


    private void startWebActivity(Gank gankItem, View itemView) {
        if (type.equals("休息视频")) {
//            VideoWebActivity.launch(getActivity(), gankItem.getUrl());
        } else if (type.equals("福利")) {

        } else {
            Intent intent = GankDetailsActivity.start((Activity) itemView.getContext(), gankItem.url, gankItem.desc, gankItem.imageUrl, gankItem.who);
            ActivityOptionsCompat mActivityOptionsCompat;
            if (Build.VERSION.SDK_INT >= 21) {
                mActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) itemView.getContext(), itemView.findViewById(R.id.image), GankDetailsActivity.TRANSIT_PIC);
            } else {
                mActivityOptionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                        itemView.findViewById(R.id.image), 0, 0,
                        itemView.findViewById(R.id.image).getWidth(),
                        itemView.findViewById(R.id.image).getHeight());
            }
            ActivityCompat.startActivity((Activity) itemView.getContext(), intent, mActivityOptionsCompat.toBundle());
        }
    }


    static class ViewHolder extends ButterKnifeViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.image)
        RatioImageView image;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}