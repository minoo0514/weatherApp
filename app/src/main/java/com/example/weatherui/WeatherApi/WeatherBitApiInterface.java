package com.example.weatherui.WeatherApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherBitApiInterface {

    @GET("forecast/daily")
    Call<WeatherBitData> getDailyForecast(
            @Query("lat") String latitude,
            @Query("lon") String longitude,
            @Query("key") String apiKey,
            @Query("days") int days
    );
}


