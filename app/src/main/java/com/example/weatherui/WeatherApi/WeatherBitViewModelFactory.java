package com.example.weatherui.WeatherApi;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class WeatherBitViewModelFactory implements ViewModelProvider.Factory {

    private final WeatherBitRepository repository;

    public WeatherBitViewModelFactory(WeatherBitRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WeatherBitViewModel.class)) {
            return (T) new WeatherBitViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
