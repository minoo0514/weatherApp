package com.example.weatherui.RoomExcel;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ExcelData.class}, version = 1)
public abstract class ExcelDataDatabase extends RoomDatabase {
    public abstract ExcelDataDao excelDataDao();
}
