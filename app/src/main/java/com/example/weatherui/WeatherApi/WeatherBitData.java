package com.example.weatherui.WeatherApi;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherBitData {

    @SerializedName("city_name")
    private String cityName;

    @SerializedName("data")
    private List<DailyForecast> data;

    public String getCityName() {
        return cityName;
    }

    public List<DailyForecast> getData() {
        return data;
    }

    public static class DailyForecast {

        @SerializedName("valid_date")
        private String validDate;

        @SerializedName("temp")
        private float temperature;

        @SerializedName("pop")
        private int precipitationProbability;

        @SerializedName("precip")
        private float precipitationAmount;

        @SerializedName("clouds")
        private int cloudCoverage;

        @SerializedName("weather")
        private Weather weather;

        public String getValidDate() {
            return validDate;
        }

        public float getTemperature() {
            return temperature;
        }

        public int getPrecipitationProbability() {
            return precipitationProbability;
        }

        public float getPrecipitationAmount() {
            return precipitationAmount;
        }

        public int getCloudCoverage() {
            return cloudCoverage;
        }

        public Weather getWeather() {
            return weather;
        }

        public static class Weather {

            @SerializedName("description")
            private String description;

            @SerializedName("icon")
            private String icon;

            public String getDescription() {
                return description;
            }

            public String getIcon() {
                return icon;
            }
        }
    }
}

