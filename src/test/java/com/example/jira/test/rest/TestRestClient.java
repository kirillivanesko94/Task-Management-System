package com.example.jira.test.rest;

import okhttp3.*;
import okhttp3.logging.*;

import java.io.*;
import java.util.concurrent.*;

public class TestRestClient {
    private final OkHttpClient client;

    private final String baseUrl;

    public TestRestClient(String baseUrl) {
        this.baseUrl = baseUrl;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        this.client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(
                100,
                1,
                TimeUnit.MINUTES
            ))
            .readTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build();
    }

    public RestResponse get(String url, String token) {
        String finalUrl = baseUrl + url;

        Request.Builder builder = new Request.Builder().url(finalUrl);

        return execRequest(addTokenIfNeededAndBuild(builder, token));
    }

    public RestResponse post(String url, String body, String token) {
        RequestBody requestBody = null;

        if (body != null) {
            requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        }

        Request.Builder builder = new Request.Builder()
            .url(baseUrl + url)
            .method("POST", requestBody);

        return execRequest(addTokenIfNeededAndBuild(builder, token));
    }
    public RestResponse delete(String url, String token) {
        String finalUrl = baseUrl + url;

        Request.Builder builder = new Request.Builder()
                .url(finalUrl)
                .delete();

        return execRequest(addTokenIfNeededAndBuild(builder, token));
    }

    public RestResponse put(String url, String body, String token) {
        RequestBody requestBody = null;

        if (body != null) {
            requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        }

        Request.Builder builder = new Request.Builder()
                .url(baseUrl + url)
                .method("PUT", requestBody);

        return execRequest(addTokenIfNeededAndBuild(builder, token));
    }
    private RestResponse execRequest(Request request) {
        try {
            try(Response resp = client.newCall(request).execute()) {
                Integer code = resp.code();
                String strBody = null;
                if (resp.body() != null) {
                    try(ResponseBody respBody = resp.body()) {
                        strBody = respBody.string();
                    }
                }

                return new RestResponse(code, strBody);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Request addTokenIfNeededAndBuild(Request.Builder builder, String token) {
        if (token != null) {
            builder
                .addHeader("Authorization", String.format("Bearer %s", token));
        }

        return builder.build();
    }
}
