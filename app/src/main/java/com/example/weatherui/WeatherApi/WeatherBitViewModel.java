package com.example.weatherui.WeatherApi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class WeatherBitViewModel extends ViewModel {

    private final WeatherBitRepository repository;
    private final LiveData<WeatherBitData> weatherData;

    // WeatherBitRepository를 사용하는 생성자
    public WeatherBitViewModel(WeatherBitRepository repository) {
        this.repository = repository;
        this.weatherData = repository.getWeatherData();
    }

    public LiveData<WeatherBitData> getWeatherData() {
        return weatherData;
    }

    public void fetchWeatherByLatLon(String latitude, String longitude, String apiKey) {
        repository.fetchWeatherByLatLon(latitude, longitude, apiKey);
    }
}

