package com.example.weatherui;

public class Event {
    private String title;
    private String date;
    private boolean isAlertEnabled;
    private boolean isTemperatureAlert;
    private boolean isRainProbabilityAlert;
    private boolean isRainAmountAlert;
    private boolean isSelected;
    private RegionData region;

    private String weatherDescription;  // 날씨 설명
    private float temperature;          // 온도
    private int precipitationProbability; // 강수 확률
    private float precipitationAmount;  // 강수량
    private int cloudCoverage;          // 구름량

    public Event(String title, String date, boolean isAlertEnabled, boolean isTemperatureAlert,
                 boolean isRainProbabilityAlert, boolean isRainAmountAlert, RegionData region) {
        this.title = title;
        this.date = date;
        this.isAlertEnabled = isAlertEnabled;
        this.isTemperatureAlert = isTemperatureAlert;
        this.isRainProbabilityAlert = isRainProbabilityAlert;
        this.isRainAmountAlert = isRainAmountAlert;
        this.isSelected = false;
        this.region = region;
    }

    // Getter 및 Setter 메서드
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public boolean isAlertEnabled() { return isAlertEnabled; }
    public boolean isTemperatureAlert() { return isTemperatureAlert; }
    public boolean isRainProbabilityAlert() { return isRainProbabilityAlert; }
    public boolean isRainAmountAlert() { return isRainAmountAlert; }
    public boolean isSelected() { return isSelected; }
    public RegionData getRegion() { return region; }

    public void setTitle(String title) { this.title = title; }
    public void setDate(String date) { this.date = date; }
    public void setAlertEnabled(boolean alertEnabled) { isAlertEnabled = alertEnabled; }
    public void setTemperatureAlert(boolean temperatureAlert) { isTemperatureAlert = temperatureAlert; }
    public void setRainProbabilityAlert(boolean rainProbabilityAlert) { isRainProbabilityAlert = rainProbabilityAlert; }
    public void setRainAmountAlert(boolean rainAmountAlert) { isRainAmountAlert = rainAmountAlert; }
    public void setSelected(boolean selected) { isSelected = selected; }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getPrecipitationProbability() {
        return precipitationProbability;
    }

    public void setPrecipitationProbability(int precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
    }

    public float getPrecipitationAmount() {
        return precipitationAmount;
    }

    public void setPrecipitationAmount(float precipitationAmount) {
        this.precipitationAmount = precipitationAmount;
    }

    public int getCloudCoverage() {
        return cloudCoverage;
    }

    public void setCloudCoverage(int cloudCoverage) {
        this.cloudCoverage = cloudCoverage;
    }
}





