package com.github.weiss.core.api;

import com.github.weiss.core.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryIntercepter implements Interceptor {

    private int mMaxRetryCount;
    private long mRetryInterval;

    public RetryIntercepter(int maxRetryCount, long retryInterval) {
        mMaxRetryCount = maxRetryCount;
        mRetryInterval = retryInterval;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = doRequest(chain, request);
        int retryNum = 0;
        while(((response==null)||!response.isSuccessful())&&retryNum<=mMaxRetryCount){
            try {
                Thread.sleep(mRetryInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retryNum++;
            LogUtils.d("response.code:"+response.code()+"  retryNum:"+retryNum);
            response = doRequest(chain, request);

        }
        return response;
    }

    private Response doRequest(Chain chain, Request request) {
        try {
            return chain.proceed(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Builder {

        private int mRetryCount = 2;
        private long mRetryInterval = 500;

        public Builder buildRetryCount(int retryCount){
            this.mRetryCount = retryCount;
            return this;
        }

        public Builder buildRetryInterval(long retryInterval){
            this.mRetryInterval = retryInterval;
            return this;
        }

        public RetryIntercepter build(){
            return new RetryIntercepter(mRetryCount,mRetryInterval);
        }

    }

}
