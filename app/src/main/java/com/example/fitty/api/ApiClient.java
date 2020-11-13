package com.example.fitty.api;

import android.content.Context;

import com.example.fitty.BuildConfig;
import com.example.fitty.models.DateFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final int CONNECT_TIMEOUT= 60;
    private static final int READ_TIMEOUT= 60;
    private static final int WRITE_TIMEOUT= 60;
    private static final String API_URL = "http://10.0.2.2:8080/api/";

    private ApiClient(){}

    public static <T> T create (Context context, Class<T> service){
        HttpLoggingInterceptor httpLoginInterceptor = new HttpLoggingInterceptor()
                .setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE ); //Esto esta bueno para ir viendo los requests
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context))
                .addInterceptor(httpLoginInterceptor).connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateFormatter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();

        return retrofit.create(service);
    }
}
