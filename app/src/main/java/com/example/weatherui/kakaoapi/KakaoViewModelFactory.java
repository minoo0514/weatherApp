package com.example.weatherui.kakaoapi;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class KakaoViewModelFactory implements ViewModelProvider.Factory {

    private final KakaoApiInterface kakaoApiService;

    public KakaoViewModelFactory(KakaoApiInterface kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(KakaoViewModel.class)) {
            return (T) new KakaoViewModel(kakaoApiService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

