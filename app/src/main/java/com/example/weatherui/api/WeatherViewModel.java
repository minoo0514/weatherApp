package com.example.weatherui.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository weatherRepository;
    private final MutableLiveData<WeatherData> weatherData = new MutableLiveData<>();

    public WeatherViewModel(WeatherApiInterface apiService) {
        this.weatherRepository = new WeatherRepository(apiService);
    }

    public LiveData<WeatherData> getWeatherData() {
        return weatherData;
    }

    public void fetchWeatherData(String serviceKey, int numOfRows, int pageNo, String dataType, String baseDate, String baseTime, int nx, int ny) {
        weatherRepository.getWeatherData(serviceKey, numOfRows, pageNo, dataType, baseDate, baseTime, nx, ny)
                .enqueue(new Callback<WeatherData>() {
                    @Override
                    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            weatherData.setValue(response.body());
                        } else {
                            Log.e("WeatherViewModel", "API Response Error: " + response.message());
                            weatherData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {
                        Log.e("WeatherViewModel", "API Request Failed", t);
                        weatherData.setValue(null);                       // 에러 처리
                    }
                });
    }
}
