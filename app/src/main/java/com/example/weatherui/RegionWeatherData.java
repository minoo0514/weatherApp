package com.example.weatherui;

import com.example.weatherui.api.WeatherData;

import java.util.Objects;

public class RegionWeatherData {
    private String regionName;        // 지역 이름
    private WeatherData.Item weatherItem;  // 날씨 데이터
    private boolean isSelected = false;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionWeatherData that = (RegionWeatherData) o;
        return Objects.equals(regionName, that.regionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionName);
    }
}


