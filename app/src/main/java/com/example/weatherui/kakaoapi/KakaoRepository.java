package com.example.weatherui.kakaoapi;

import retrofit2.Call;

public class KakaoRepository {

    private final KakaoApiInterface kakaoApiService;

    public KakaoRepository(KakaoApiInterface kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    public Call<KakaoResponse> getRegionName(double longitude, double latitude) {
        String authorization = "KakaoAK " + "cbb9be7924aa961f13b0731f173c9aee";  // 실제 카카오 REST API 키를 여기에 입력하세요
        return kakaoApiService.getRegionName(authorization, longitude, latitude);
    }
}
