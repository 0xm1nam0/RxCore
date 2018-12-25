package com.github.weiss.example.api;

import com.github.weiss.example.HttpResult;
import com.github.weiss.example.entity.Gank;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("data/福利/10/{page}")
    Flowable<HttpResult<List<Gank>>> getMeizhiData(
            @Path("page") int page);

    @GET("random/data/福利/10")
    Flowable<HttpResult<List<Gank>>> getRandMeizhiData();

    @GET("data/{gank}/10/{page}")
    Observable<HttpResult<List<Gank>>> getGankData(@Path("gank") String gank,
                                                       @Path("page") int page);

    @GET("search/query/{query}/category/{category}/count/10/page/{page}")
    Flowable<HttpResult<List<Gank>>> getSearch(@Path("query") String query, @Path("category") String category,
                                                   @Path("page") int page);

    @GET("data/福利/10/{page}")
    Flowable<HttpResult<List<Gank>>> getMeizhiList(@Path("page") int page);
}
