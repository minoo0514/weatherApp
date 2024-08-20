package com.example.weatherui.resource;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class WeatherTemp {

    public String[] lookWeatherTemp(String date) throws IOException, JSONException {

        String basedate = date;

        String basicUrl = "https://apis.data.go.kr/1360000/LivingWthrIdxServiceV4/getSenTaIdxV4";
        // 내가 받은 인증키
        String serviceKey = "E9YTwJF5HtPr5xipNzQvR1AaxrTXHsiPR9TBJAYYlINbSj0XzJZAZkEhfSZXaTQB8v8JWgXbazVcEFK72vAXMw%3D%3D";

        StringBuilder urlBuilder = new StringBuilder(basicUrl); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)*/
        urlBuilder.append("&" + URLEncoder.encode("areaNo", "UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); /*지점코드*/
        urlBuilder.append("&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(basedate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("requestCode", "UTF-8") + "=" + URLEncoder.encode("A41", "UTF-8")); /*서비스대상 요청코드*/

        URL url = new URL(urlBuilder.toString());
        // json데이터들을 웹페이지를통해 확인할 수 있게  로그캣에 링크 출력
        System.out.println("url : " + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result = sb.toString();

        // response 키를 가지고 데이터를 파싱
        JSONObject jsonObj_1 = new JSONObject(result);
        String response = jsonObj_1.getString("response");

        // response 로 부터 body 찾기
        JSONObject jsonObj_2 = new JSONObject(response);
        String body = jsonObj_2.getString("body");

        // body 로 부터 items 찾기
        JSONObject jsonObj_3 = new JSONObject(body);
        String items = jsonObj_3.getString("items");
        Log.i("ITEMS", items);

        // items로 부터 itemlist 를 받기
        JSONObject jsonObj_4 = new JSONObject(items);
        JSONArray jsonArray = jsonObj_4.getJSONArray("item");

        String[] windChillTemp = null;
        if (jsonArray.length() > 0) {
            JSONObject weatherItem = jsonArray.getJSONObject(0); // 첫 번째 객체를 가져옵니다.

            // 78개의 h 값을 저장할 배열
            windChillTemp = new String[78];

            // h1부터 h78까지 배열에 저장
            for (int i = 0; i < windChillTemp.length; i++) {
                String key = "h" + (i + 1);
                windChillTemp[i] = weatherItem.optString(key, ""); // 키가 없을 경우 빈 문자열 삽입
            }

            // 배열 내용을 로그에 기록
            for (int i = 0; i < windChillTemp.length; i++) {
                Log.d("WeatherData", "h" + (i + 1) + ": " + windChillTemp[i]);
            }
        }
        return windChillTemp;
    }
}


