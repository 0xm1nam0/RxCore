/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.weiss.example.api;


import com.github.weiss.core.entity.HttpResult;
import com.github.weiss.example.entity.Gank;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GankApiService {

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
