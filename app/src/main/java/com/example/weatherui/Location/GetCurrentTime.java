package com.example.weatherui.Location;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GetCurrentTime {
    public String getCurrentDate() {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int adjustedHour = (hour / 3) * 3;  // 3시간 간격으로 조정

        return String.format(Locale.getDefault(), "%02d00", adjustedHour);
    }
}
