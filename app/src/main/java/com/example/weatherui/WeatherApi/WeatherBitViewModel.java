package com.example.weatherui.WeatherApi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class WeatherBitViewModel extends ViewModel {

    private WeatherBitRepository repository;
    private LiveData<WeatherBitData> weatherData;

    public WeatherBitViewModel(WeatherBitRepository repository) {
        this.repository = repository;
        this.weatherData = repository.getWeatherData();
    }

    public LiveData<WeatherBitData> getWeatherData() {
        return weatherData;
    }

    public void fetchWeatherByLatLon(String latitude, String longitude, String apiKey, int days) {
        repository.fetchWeatherByLatLon(latitude, longitude, apiKey, days);
    }
}
