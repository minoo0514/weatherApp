package com.example.weatherui.api;

import retrofit2.Call;

public class WeatherRepository {

    private final WeatherApiInterface apiService;

    public WeatherRepository(WeatherApiInterface apiService) {
        this.apiService = apiService;
    }

    public Call<WeatherData> getWeatherData(String serviceKey, int numOfRows, int pageNo, String dataType, String baseDate, String baseTime, int nx, int ny) {
        return apiService.getWeatherData(serviceKey, numOfRows, pageNo, dataType, baseDate, baseTime, nx, ny);
    }
}
