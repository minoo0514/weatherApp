package com.example.weatherui.kakaoapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KakaoRetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://dapi.kakao.com/";  // API의 기본 URL

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
