package com.github.weiss.example.entity;

import com.github.weiss.core.entity.ListEntity;
import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.helper.RxSchedulers;
import com.github.weiss.example.HttpResult;
import com.github.weiss.example.api.Api;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

/**
 * Created by Weiss on 2017/1/20.
 */

public class Gank extends ListEntity {

    public String _id;

    public String createdAt;

    public String desc;

    public String publishedAt;

    public String source;

    public String type;

    public String url;

    public String imageUrl;

    public boolean used;

    public String who;

    public List<String> images;

    @Override
    public Observable<HttpResult<List<Gank>>> getPage(int page) {
        return Api.getInstance().service.getGankData(param.get("gank"), page)
                .zipWith(Api.getInstance().service.getGankData("福利", page),
                        (BiFunction<HttpResult<List<Gank>>, HttpResult<List<Gank>>, HttpResult<List<Gank>>>) (listHttpResult, listHttpResult2) -> {
                            HttpResult zipItems = new HttpResult();
                            Gank zipItem;
                            List<Gank> zipResults = new ArrayList<Gank>();

                            for (int i = 0; i < listHttpResult2.results.size(); i++) {
                                zipItem = new Gank();
                                Gank item = listHttpResult2.results.get(i);
                                Gank gankInfo = listHttpResult.results.get(i);
                                if (CollectionUtils.isEmpty(gankInfo.images)) {
                                    zipItem.imageUrl = item.url;
                                } else {
                                    zipItem.imageUrl = gankInfo.images.get(0);
                                }
                                zipItem.url = gankInfo.url;
                                zipItem.desc = gankInfo.desc;
                                zipItem.who = gankInfo.who;
                                zipResults.add(zipItem);
                            }
                            zipItems.results = zipResults;
                            return zipItems;
                        })
                .compose(RxSchedulers.io_main());
    }

}
