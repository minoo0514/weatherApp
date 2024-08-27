package com.example.weatherui.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiInterface {
    @GET("VilageFcstInfoService_2.0/getVilageFcst")
    Call<WeatherData> getWeatherData(
            @Query("serviceKey") String serviceKey,
            @Query("numOfRows") int numOfRows,
            @Query("pageNo") int pageNo,
            @Query("dataType") String dataType,
            @Query("base_date") String baseDate,
            @Query("base_time") String baseTime,
            @Query("nx") String nx,
            @Query("ny") String ny
    );

    @GET("LivingWthrIdxServiceV4/getSenTaIdxV4")
    Call<WindChillData> getWindChill(
            @Query("serviceKey") String serviceKey,
            @Query("numOfRows") int numOfRows,
            @Query("pageNo") int pageNo,
            @Query("dataType") String dataType,
            @Query("areaNo") String areaNo,
            @Query("time") String time,
            @Query("requestCode") String requestCode
    );
}
