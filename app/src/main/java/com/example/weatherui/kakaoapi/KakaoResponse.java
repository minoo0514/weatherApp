package com.example.weatherui.kakaoapi;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KakaoResponse {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("documents")
    public List<Document> documents;

    public static class Meta {
        @SerializedName("total_count")
        public int totalCount;
    }

    public static class Document {
        @SerializedName("region_type")
        public String regionType;

        @SerializedName("address_name")
        public String addressName;

        @SerializedName("region_1depth_name")
        public String region1DepthName;

        @SerializedName("region_2depth_name")
        public String region2DepthName;

        @SerializedName("region_3depth_name")
        public String region3DepthName;

        @SerializedName("region_4depth_name")
        public String region4DepthName;

        @SerializedName("code")
        public String code;

        @SerializedName("x")
        public double x;

        @SerializedName("y")
        public double y;
    }

    public String getRegion3DepthName() {
        if (documents != null && !documents.isEmpty()) {
            return documents.get(0).region3DepthName;
        }
        return "Unknown Location";  // 데이터가 없을 경우 기본값
    }

    // 특정 address_name을 반환하는 메서드
    public String getAddressName() {
        if (documents != null && !documents.isEmpty()) {
            return documents.get(0).addressName;
        } else {
            Log.e("KakaoResponse", "No documents available");
        }
        return null; // 데이터가 없을 경우
    }

    // 특정 region_1depth_name, region_2depth_name, region_3depth_name을 반환하는 메서드
    public String getRegionName() {
        if (documents != null && !documents.isEmpty()) {
            Document doc = documents.get(0);
            return doc.region1DepthName + " " + doc.region2DepthName + " " + doc.region3DepthName;
        } else {
            Log.e("KakaoResponse", "No documents available");
        }
        return null; // 데이터가 없을 경우
    }

    // 특정 region_type에 해당하는 region 정보를 반환하는 메서드
    public Document getRegionByType(String regionType) {
        if (documents != null) {
            for (Document doc : documents) {
                if (doc.regionType.equals(regionType)) {
                    return doc;
                }
            }
            Log.d("KakaoResponse", "No matching region found for type: " + regionType);
        } else {
            Log.e("KakaoResponse", "Documents is null");
        }
        return null; // 해당하는 데이터가 없을 경우
    }
}
