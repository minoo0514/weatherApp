package com.example.weatherui.WeatherApi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherBitRepository {

    private WeatherBitApiInterface weatherApi;
    private MutableLiveData<WeatherBitData> weatherData = new MutableLiveData<>();

    public WeatherBitRepository(WeatherBitApiInterface weatherApi) {
        this.weatherApi = weatherApi;
    }

    public LiveData<WeatherBitData> getWeatherData() {
        return weatherData;
    }

    public void fetchWeatherByLatLon(String latitude, String longitude, String apiKey, int days) {
        weatherApi.getDailyForecast(latitude, longitude, apiKey, days).enqueue(new Callback<WeatherBitData>() {
            @Override
            public void onResponse(Call<WeatherBitData> call, Response<WeatherBitData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    weatherData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherBitData> call, Throwable t) {
                weatherData.setValue(null);
            }
        });
    }
}
