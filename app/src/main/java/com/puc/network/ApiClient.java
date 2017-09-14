package com.puc.network;

import android.app.Activity;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {


    //live url
    public static String BASE_URL = "http://mycardsecured.com/mobileapi/api/v1/";

    //test url
    //public static String BASE_URL = "http://mycardsecured.aipxperts.com:8080/api/v1/";

    private static Retrofit retrofit = null;




    public static Retrofit getClient(Activity context) {



        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30000, TimeUnit.SECONDS)
                .addInterceptor(new ConnectivityInterceptor(context))
                .readTimeout(30000, TimeUnit.SECONDS).build();

        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …
// add logging as last interceptor
            httpClient.addInterceptor(logging); // <-- this is the important line!

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }


}
