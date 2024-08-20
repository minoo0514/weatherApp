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

public class WeatherData {

    private String sky, temp, rain, rainPossibility, snow, humidity, maxTemp, minTemp;
    private String weatherConditionCode;

    public String lookWeather(String date, String time, String nx, String ny) throws IOException, JSONException {
        String baseDate = date; // 2022xxxx 형식을 사용
        String baseTime = timeChangeData(time); // 0x00 형식을 사용
        String type = "json";

        // end point 주소값
        String basicUrl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        // 내가 받은 인증키
        String serviceKey = "64Oz35s2lkhPlO%2BzpJgRbTmVIM7uM2wXHG%2BGILkxH%2FvmiOS9IKd%2Ff9ZlEXDcuosyguY5Xuw7BOhpy%2BW5Veaaug%3D%3D";

        StringBuilder urlBuilder = new StringBuilder(basicUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey); // 서비스 키
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));    /* 타입 */
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고 싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고 싶은 시간 오전 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); // x좌표
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); // y좌표

        URL url = new URL(urlBuilder.toString()); // URL객체 생성
        System.out.println("url : " + url);  // 로그캣에 링크 출력
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //url이 참조하는 리소스에 대한 연결을 엶 URLConnection 객체 반환
        conn.setRequestMethod("GET"); // 요청방식 지정된 리소스로부터 데이터를 요청하는데 사용(데이터 검색)
        conn.setRequestProperty("Content-type", "application/json"); // 데이터 형식 설정

        BufferedReader rd; // 문자를 효율적으로 읽을 수 있도록 해주는 클래스 서버로부터 받은 응답 데이터를 읽기 위해 사용
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) { //응답 데이터를 줄 단위로 읽어와서 StringBuilder에 추가
            sb.append(line);
        }
        rd.close(); // 스트림 해제
        conn.disconnect(); // HttpURLConnection을 닫아 서버와의 연결 해제
        String result = sb.toString(); // 응답 데이터들을 String으로 변환하여 결과로 저장



        JSONObject jsonObj_1 = new JSONObject(result); // JSON 형식의 응답 문자열을 JSONObject로 변환
        String response = jsonObj_1.getString("response"); // "response" 키의 값을 문자열로 추출

        JSONObject jsonObj_2 = new JSONObject(response); // "response" 문자열을 다시 JSONObject로 변환
        String body = jsonObj_2.getString("body"); // "body" 키의 값을 문자열로 추출

        JSONObject jsonObj_3 = new JSONObject(body); // "body" 문자열을 다시 JSONObject로 변환
        String items = jsonObj_3.getString("items"); // "items" 키의 값을 문자열로 추출

        JSONObject jsonObj_4 = new JSONObject(items); // "items" 문자열을 다시 JSONObject로 변환
        JSONArray jsonArray = jsonObj_4.getJSONArray("item"); // "item" 키의 값을 JSONArray로 추출

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObj_4 = jsonArray.getJSONObject(i);
            String fcstValue = jsonObj_4.getString("fcstValue");
            String category = jsonObj_4.getString("category");

            if (category.equals("SKY")) {
                if (fcstValue.equals("1")) {
                    sky = "맑음 ";
                    weatherConditionCode = "1";
                } else if (fcstValue.equals("3")) {
                    sky = "구름많음 ";
                    weatherConditionCode = "3";
                } else if (fcstValue.equals("4")) {
                    sky = "흐림 ";
                    weatherConditionCode = "4";
                }
            }

            if (category.equals("TMP")) {
                temp = fcstValue + "° ";
            }
            if (category.equals("PCP")) {
                rain = fcstValue + "mm ";
            }
            if(category.equals("POP")) {
                rainPossibility = fcstValue + "% ";
            }
            if(category.equals("SNO")) {
                snow = fcstValue + " ";
            }
            if(category.equals("REH")) {
                humidity = fcstValue + "% ";
            }
            if(category.equals("TMX")) {
                maxTemp = fcstValue + "° ";
            }
            if(category.equals("TMN")) {
                minTemp = fcstValue + "°";
            }
        }
        return sky + rain + rainPossibility + temp + snow + humidity + maxTemp + minTemp;
    }

    public String timeChangeData(String time)
    {
        switch(time) {

            case "0200":
            case "0300":
            case "0400":
                time = "0200";
                break;
            case "0500":
            case "0600":
            case "0700":
                time = "0500";
                break;
            case "0800":
            case "0900":
            case "1000":
                break;
            case "1100":
            case "1200":
            case "1300":
                time = "1100";
                break;
            case "1400":
            case "1500":
            case "1600":
                time = "1400";
                break;
            case "1700":
            case "1800":
            case "1900":
                time = "1700";
                break;
            case "2000":
            case "2100":
            case "2200":
                time = "2000";
                break;
            case "2300":
            case "0000":
            case "0100":
                time = "2300";

        }
        return time;
    }
}
