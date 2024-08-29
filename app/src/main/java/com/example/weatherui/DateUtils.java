package com.example.weatherui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    // 날짜를 "yyyy년 M월 d일 a h시" 형식에서 "M/d" 형식으로 변환하는 메서드
    public static String formatDate(String dateStr) {

        dateStr = dateStr.trim();

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시");  // 원래의 날짜 형식
        SimpleDateFormat targetFormat = new SimpleDateFormat("M/d");  // 목표 날짜 형식

        try {
            Date date = originalFormat.parse(dateStr);  // 문자열을 Date 객체로 변환
            return targetFormat.format(date);  // 목표 형식으로 변환하여 반환
        } catch (ParseException e) {
            e.printStackTrace();
            return "";  // 변환에 실패하면 빈 문자열 반환
        }
    }
}

