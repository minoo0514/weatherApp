package com.example.weatherui.Location;

import android.util.Log;

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
        Log.d("GetCurrentTime", "getCurrentTime: " + hour);

        // 가장 가까운 3시간 단위로 조정
        int adjustedHour = (hour / 3) * 3;

        // 3시간 단위로 조정한 시간값을 특정 출력 포맷에 맞춰 매핑
        switch (adjustedHour) {
            case 0:
            case 1:
            case 2:
            case 3:
                return "0200";
            case 4:
            case 5:
            case 6:
                return "0500";
            case 7:
            case 8:
            case 9:
                return "0800";
            case 10:
            case 11:
            case 12:
                return "1100";
            case 13:
            case 14:
            case 15:
                return "1400";
            case 16:
            case 17:
            case 18:
                return "1700";
            case 19:
            case 20:
            case 21:
                return "2000";
            case 22:
            case 23:
            case 24: // midnight case handled here
                return "2300";
            default:
                return "0000"; // Default case if something goes wrong
        }
    }
}
