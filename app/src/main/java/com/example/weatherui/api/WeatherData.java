package com.example.weatherui.api;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherData {

    @SerializedName("response")
    public Response response;

    public static class Response {
        @SerializedName("header")
        public Header header;

        @SerializedName("body")
        public Body body;
    }

    public static class Header {
        @SerializedName("resultCode")
        public String resultCode;

        @SerializedName("resultMsg")
        public String resultMsg;
    }

    public static class Body {
        @SerializedName("dataType")
        public String dataType;

        @SerializedName("items")
        public Items items;

        @SerializedName("numOfRows")
        public int numOfRows;

        @SerializedName("pageNo")
        public int pageNo;

        @SerializedName("totalCount")
        public int totalCount;
    }

    public static class Items {
        @SerializedName("item")
        public List<Item> itemList;
    }

    public static class Item {
        @SerializedName("baseDate")
        public String baseDate;

        @SerializedName("baseTime")
        public String baseTime;

        @SerializedName("category")
        public String category;

        @SerializedName("fcstDate")
        public String fcstDate;

        @SerializedName("fcstTime")
        public String fcstTime;

        @SerializedName("fcstValue")
        public String fcstValue;

        @SerializedName("nx")
        public int nx;

        @SerializedName("ny")
        public int ny;
    }

    // 특정 category, fcstDate, fcstTime에 해당하는 fcstValue를 반환하는 메서드
    public String getFcstValue(String category, String fcstDate, String fcstTime) {
        if (response != null && response.body != null && response.body.items != null) {
            for (Item item : response.body.items.itemList) {
                Log.d("WeatherData", "Checking item: " + item.category + ", " + item.fcstDate + ", " + item.fcstTime);
                if (item.category.equals(category) && item.fcstDate.equals(fcstDate) && item.fcstTime.equals(fcstTime)) {
                    Log.d("WeatherData", "Found matching item: " + item.fcstValue);
                    return item.fcstValue;
                }
            }
            Log.d("WeatherData", "No matching item found for: " + category + ", " + fcstDate + ", " + fcstTime);
        } else {
            Log.e("WeatherData", "Response or body or items is null");
        }
        return null; // 해당하는 데이터가 없을 경우
    }

    // 특정 fcstDate와 fcstTime에 대한 모든 카테고리 값을 반환하는 메서드
    public List<Item> getForecastForDateTime(String fcstDate, String fcstTime) {
        List<Item> result = new ArrayList<>();
        if (response != null && response.body != null && response.body.items != null) {
            for (Item item : response.body.items.itemList) {
                if (item.fcstDate.equals(fcstDate) && item.fcstTime.equals(fcstTime)) {
                    result.add(item);
                }
            }
        }
        return result;
    }
}
