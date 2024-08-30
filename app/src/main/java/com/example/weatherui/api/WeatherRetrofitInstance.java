package com.example.weatherui.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRetrofitInstance {

    private static Retrofit weatherretrofit = null;
    private static final String WEATHER_BASE_URL = "https://apis.data.go.kr/1360000/";

    public static Retrofit getWeatherRetrofitInstance() {

        if (weatherretrofit == null) {
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

            weatherretrofit = new Retrofit.Builder()
                    .baseUrl(WEATHER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())  // Add custom OkHttpClient
                    .build();
        }
        return weatherretrofit;
    }
}
