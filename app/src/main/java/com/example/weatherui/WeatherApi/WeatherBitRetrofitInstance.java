package com.example.weatherui.WeatherApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherBitRetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.weatherbit.io/v2.0/";

    public static Retrofit getWeatherRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
