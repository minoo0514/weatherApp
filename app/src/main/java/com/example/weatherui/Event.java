package com.example.weatherui;

public class Event {
    private String title;
    private String date;
    private boolean isAlertEnabled;  // 전체 알림 온오프
    private boolean isTemperatureAlert;
    private boolean isRainProbabilityAlert;
    private boolean isRainAmountAlert;
    private boolean isSelected;
    private RegionData region; // 지역 정보 추가


    public Event(String title, String date, boolean isAlertEnabled, boolean isTemperatureAlert, boolean isRainProbabilityAlert, boolean isRainAmountAlert, RegionData region) {
        this.title = title;
        this.date = date;
        this.isAlertEnabled = isAlertEnabled;
        this.isTemperatureAlert = isTemperatureAlert;
        this.isRainProbabilityAlert = isRainProbabilityAlert;
        this.isRainAmountAlert = isRainAmountAlert;
        this.isSelected = false;
        this.region = region;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public boolean isAlertEnabled() { return isAlertEnabled; }
    public boolean isTemperatureAlert() { return isTemperatureAlert; }
    public boolean isRainProbabilityAlert() { return isRainProbabilityAlert; }
    public boolean isRainAmountAlert() { return isRainAmountAlert; }
    public boolean isSelected() { return isSelected; }  // 선택 여부 반환

    public void setTitle(String title) { this.title = title; }
    public void setDate(String date) { this.date = date; }
    public void setAlertEnabled(boolean alertEnabled) { isAlertEnabled = alertEnabled; }
    public void setTemperatureAlert(boolean temperatureAlert) { isTemperatureAlert = temperatureAlert; }
    public void setRainProbabilityAlert(boolean rainProbabilityAlert) { isRainProbabilityAlert = rainProbabilityAlert; }
    public void setRainAmountAlert(boolean rainAmountAlert) { isRainAmountAlert = rainAmountAlert; }
    public void setSelected(boolean selected) { isSelected = selected; }

    public RegionData getRegion() {
        return region;
    }
}




