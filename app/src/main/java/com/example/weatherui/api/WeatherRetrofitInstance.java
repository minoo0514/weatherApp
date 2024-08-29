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

    private static Retrofit kakaoRetrofit = null;
    private static final String KAKAO_BASE_URL = "https://dapi.kakao.com/";


    public static Retrofit getWeatherRetrofitInstance() {

        if (weatherretrofit == null) {
            // Logging interceptor for monitoring network calls
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttpClient with custom timeout settings
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)  // Connection timeout
                    .readTimeout(30, TimeUnit.SECONDS)     // Read timeout
                    .writeTimeout(30, TimeUnit.SECONDS)    // Write timeout
                    .addInterceptor(logging);              // Add logging interceptor

            // Gson instance with lenient parsing
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Retrofit instance
            weatherretrofit = new Retrofit.Builder()
                    .baseUrl(WEATHER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())  // Add custom OkHttpClient
                    .build();
        }
        return weatherretrofit;
    }


    public static Retrofit getKakaoRetrofitInstance() {

        if (kakaoRetrofit == null) {
            // Logging interceptor for monitoring network calls
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttpClient with custom timeout settings
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)  // Connection timeout
                    .readTimeout(30, TimeUnit.SECONDS)     // Read timeout
                    .writeTimeout(30, TimeUnit.SECONDS)    // Write timeout
                    .addInterceptor(logging);              // Add logging interceptor

            // Gson instance with lenient parsing
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Retrofit instance for Kakao API
            kakaoRetrofit = new Retrofit.Builder()
                    .baseUrl(KAKAO_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())  // Add custom OkHttpClient
                    .build();
        }
        return kakaoRetrofit;
    }
}
