package com.example.weatherui.WeatherApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherBitRetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.weatherbit.io/v2.0/";

    public static Retrofit getWeatherRetrofitInstance() {

        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)  // Connection timeout
                    .readTimeout(30, TimeUnit.SECONDS)     // Read timeout
                    .writeTimeout(30, TimeUnit.SECONDS)    // Write timeout
                    .addInterceptor(logging);              // Add logging interceptor

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())  // Add custom OkHttpClient
                    .build();
        }
        return retrofit;
    }
}
