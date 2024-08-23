package com.example.weatherui.RoomSelectRegion;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SelectedRegionDao {
    @Insert
    void insert(SelectedRegion selectedRegion);

    @Query("SELECT * FROM selected_region")
    List<SelectedRegion> getAllSelectedRegions();
}

