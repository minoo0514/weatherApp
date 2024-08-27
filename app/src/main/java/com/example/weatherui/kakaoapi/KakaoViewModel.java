package com.example.weatherui.kakaoapi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherui.api.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KakaoViewModel extends ViewModel {

    private final KakaoRepository kakaoRepository;
    private final MutableLiveData<String> region3DepthName = new MutableLiveData<>();

    public KakaoViewModel(KakaoApiInterface kakaoApiService) {
        kakaoRepository = new KakaoRepository(kakaoApiService);
    }

    public LiveData<String> getRegion3DepthName() {
        return region3DepthName;
    }

    public void fetchRegionName(double longitude, double latitude) {
        kakaoRepository.getRegionName(longitude, latitude).enqueue(new Callback<KakaoResponse>() {
            @Override
            public void onResponse(Call<KakaoResponse> call, Response<KakaoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String regionName = response.body().getRegion3DepthName();
                    region3DepthName.setValue(regionName);
                } else {
                    region3DepthName.setValue("Failed to fetch region name");
                }
            }

            @Override
            public void onFailure(Call<KakaoResponse> call, Throwable t) {
                region3DepthName.setValue("Network error");
            }
        });
    }
}
