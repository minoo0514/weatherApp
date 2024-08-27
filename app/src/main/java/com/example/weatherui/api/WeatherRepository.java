package com.example.weatherui.api;

import com.example.weatherui.kakaoapi.KakaoApiInterface;

import retrofit2.Call;

public class WeatherRepository {

    private final WeatherApiInterface apiService;

    public WeatherRepository(WeatherApiInterface apiService) {
        this.apiService = apiService;
    }

    public Call<WeatherData> getWeatherData(String serviceKey, int numOfRows, int pageNo, String dataType, String baseDate, String baseTime, String nx, String ny) {
        return apiService.getWeatherData(serviceKey, numOfRows, pageNo, dataType, baseDate, baseTime, nx, ny);
    }

    public Call<WindChillData> getWindChillData(String serviceKey, int numOfRows, int pageNo, String dataType, String areaNo, String time, String requestCode) {
        return apiService.getWindChill(serviceKey, numOfRows, pageNo, dataType, areaNo, time, requestCode);
    }
}
