package com.krews.krews;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static Retrofit retrofit=null;

    public static Retrofit getClient(String url, Context context){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder authAPIHttpClientBuilder = new OkHttpClient.Builder();

        authAPIHttpClientBuilder.connectTimeout(3, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(3, TimeUnit.MINUTES) // write timeout
                .readTimeout(3, TimeUnit.MINUTES); // read timeout

        authAPIHttpClientBuilder.addInterceptor(interceptor);

        retrofit=new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).client(authAPIHttpClientBuilder.build()).build();

        return retrofit;
    }
}
