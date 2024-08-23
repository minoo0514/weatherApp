package com.example.weatherui.RoomExcel;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExcelDataDao {
    @Insert
    void insertAll(List<ExcelData> excelDataList);

    @Insert
    void insert(ExcelData excelData);

    @Query("SELECT * FROM excel_data")
    List<ExcelData> getAll();

    @Query("SELECT * FROM excel_data WHERE step1 LIKE :query OR step2 LIKE :query OR step3 LIKE :query")
    List<ExcelData> searchLocations(String query);
}
