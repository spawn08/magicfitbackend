package com.spawnai.middleware.utils;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class BotInterceptors implements Interceptor {

    private String USERNAME = "spawnai";
    private String PASSWORD = "spawn1992";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request proceed = request.newBuilder()
                .addHeader("Authorization", Credentials.basic(USERNAME, PASSWORD))
                .build();
        return chain.proceed(proceed);
    }
}
