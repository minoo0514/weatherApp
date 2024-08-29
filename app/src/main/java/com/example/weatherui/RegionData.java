package com.example.weatherui;

public class RegionData {
    private String name; // 지역이름
    private String code; // 행정코드
    private String nx; // 격자 x 좌표
    private String ny; // 격자 y 좌표

    public RegionData(String name, String code, String nx,String ny) {
        this.name = name;
        this.code = code;
        this.nx = nx;
        this.ny = ny;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getNx() {
        return nx;
    }

    public String getNy() {
        return ny;
    }

}
