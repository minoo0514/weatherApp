package com.example.weatherui;

import com.example.weatherui.api.WeatherData;

public class RegionWeatherData {
    private String regionName;        // 지역 이름
    private WeatherData.Item weatherItem;  // 날씨 데이터

    public RegionWeatherData(String regionName, WeatherData.Item weatherItem) {
        this.regionName = regionName;
        this.weatherItem = weatherItem;
    }

    public String getRegionName() {
        return regionName;
    }

    public WeatherData.Item getWeatherItem() {
        return weatherItem;
    }
}

