package com.example.weatherui.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherui.RegionWeatherData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository weatherRepository;
    private final MutableLiveData<WeatherData> weatherData = new MutableLiveData<>();
    private final MutableLiveData<WindChillData> windChillData = new MutableLiveData<>();
    private final MutableLiveData<List<RegionWeatherData>> regionWeatherItems = new MutableLiveData<>();


    public WeatherViewModel(WeatherApiInterface apiService) {
        this.weatherRepository = new WeatherRepository(apiService);
    }

    public LiveData<WeatherData> getWeatherData() {
        return weatherData;
    }

    public LiveData<WindChillData> getWindChillData() {
        return windChillData;
    }

    public LiveData<List<RegionWeatherData>> getRegionWeatherItems() {
        return regionWeatherItems;
    }

    public void fetchWeatherData(String serviceKey, int numOfRows, int pageNo, String dataType, String baseDate, String baseTime, String nx, String ny) {
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

    public void fetchWindChillData(String serviceKey, int numOfRows, int pageNo, String dataType, String areaNo, String time, String requestCode) {
        weatherRepository.getWindChillData(serviceKey, numOfRows, pageNo, dataType, areaNo, time, requestCode)
                .enqueue(new Callback<WindChillData>() {
                    @Override
                    public void onResponse(Call<WindChillData> call, Response<WindChillData> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            windChillData.setValue(response.body());
                        } else {
                            Log.e("WeatherViewModel", "API Response Error: " + response.message());
                            windChillData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<WindChillData> call, Throwable t) {
                        Log.e("WeatherViewModel", "API Request Failed", t);
                        windChillData.setValue(null);  // 에러 처리
                    }
                });
    }

    public void fetchRegionWeatherData(String regionName, String serviceKey, int numOfRows, int pageNo, String dataType, String baseDate, String baseTime, String nx, String ny) {
        weatherRepository.getWeatherData(serviceKey, numOfRows, pageNo, dataType, baseDate, baseTime, nx, ny)
                .enqueue(new Callback<WeatherData>() {
                    @Override
                    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("fetchRegionWeatherData", "Response body: " + response.body().toString());
                            String currentTime = getNextHour(baseTime);
                            List<WeatherData.Item> forecastItems = response.body().getForecastForDateTime(baseDate, currentTime);
                            if (forecastItems != null && !forecastItems.isEmpty()) {
                                Log.d("fetchRegionWeatherData", "Forecast items count: " + forecastItems.size());
                                List<RegionWeatherData> items = new ArrayList<>();
                                for (WeatherData.Item item : forecastItems) {
                                    items.add(new RegionWeatherData(regionName, item));
                                    Log.d("fetchRegionWeatherData", "Added item: " + item.toString());
                                }
                                // LiveData 업데이트
                                regionWeatherItems.setValue(items);
                            } else {
                                Log.e("fetchRegionWeatherData", "No forecast items available for the given date and time.");
                            }
                        } else {
                            Log.e("WeatherViewModel", "API Response Error: " + response.message());
                            regionWeatherItems.setValue(new ArrayList<>()); // 오류 시 빈 리스트 설정
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {
                        Log.e("WeatherViewModel", "API Request Failed", t);
                        regionWeatherItems.setValue(new ArrayList<>()); // 실패 시 빈 리스트 설정
                    }
                });
    }

    private String getNextHour(String currentTime) {
        // currentTime을 정수로 변환하여 100을 더합니다.
        int timeInt = Integer.parseInt(currentTime);
        timeInt += 100;

        // 만약 2400을 초과하는 경우, 시간 값을 0000으로 리셋 (24시간 포맷 유지)
        if (timeInt >= 2400) {
            timeInt -= 2400;
        }

        // 다시 문자열 형식으로 변환하고 앞자리가 0으로 시작할 경우, 형식 유지
        return String.format("%04d", timeInt);
    }
}