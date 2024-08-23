package com.example.weatherui.RoomSelectRegion;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SelectedRegion.class}, version = 1)
public abstract class SelectedRegionDatabase extends RoomDatabase {
    public abstract SelectedRegionDao selectedRegionDao();
}

