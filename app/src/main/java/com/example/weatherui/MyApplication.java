package com.example.weatherui;

import android.app.Application;

import android.util.Log;

import androidx.room.Room;

import com.example.weatherui.RoomExcel.ExcelData;
import com.example.weatherui.RoomExcel.ExcelDataDatabase;
import com.example.weatherui.RoomExcel.ExcelReader;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 엑셀 데이터를 데이터베이스에 저장하는 메서드 호출
        //saveExcelDataToDatabase();
    }

    private void saveExcelDataToDatabase() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ExcelDataDatabase db = Room.databaseBuilder(getApplicationContext(),
                        ExcelDataDatabase.class, "excel-data-database").build();

                ExcelReader excelReader = new ExcelReader();
                try {
                    InputStream inputStream = getAssets().open("locations.xlsx");
                    List<ExcelData> excelDataList = excelReader.readLocationsFromExcel(inputStream);
                    db.excelDataDao().insertAll(excelDataList);

                    // 삽입된 데이터 확인 로그
                    Log.d("saveExcelData", "Inserted data count: " + excelDataList.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

