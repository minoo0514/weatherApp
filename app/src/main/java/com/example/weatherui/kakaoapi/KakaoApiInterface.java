package com.example.weatherui.kakaoapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface KakaoApiInterface {
    @GET("/v2/local/geo/coord2regioncode.json")
    Call<KakaoResponse> getRegionName(
            @Header("Authorization") String authorization,
            @Query("x") double longitude,
            @Query("y") double latitude
    );
}
