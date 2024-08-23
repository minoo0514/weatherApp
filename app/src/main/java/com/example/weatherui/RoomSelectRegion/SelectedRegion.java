package com.example.weatherui.RoomSelectRegion;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "selected_region")
public class SelectedRegion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String regionName; // 예: 서울특별시 종로구 사직동
    private String step1;
    private String step2;
    private String step3;
    private double longitude;
    private double latitude;

    // Getter와 Setter 메서드들
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getStep1() {
        return step1;
    }

    public void setStep1(String step1) {
        this.step1 = step1;
    }

    public String getStep2() {
        return step2;
    }

    public void setStep2(String step2) {
        this.step2 = step2;
    }

    public String getStep3() {
        return step3;
    }

    public void setStep3(String step3) {
        this.step3 = step3;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

