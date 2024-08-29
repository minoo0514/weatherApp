package com.example.weatherui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RegionViewModel extends AndroidViewModel {

    private final MutableLiveData<List<RegionWeatherData>> regionWeatherDataList = new MutableLiveData<>();
    private final SharedPreferences sharedPreferences;

    public RegionViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("regions", Context.MODE_PRIVATE);
        loadRegions();
    }

    public LiveData<List<RegionWeatherData>> getRegionWeatherDataList() {
        return regionWeatherDataList;
    }

    private void loadRegions() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("region_list", null);
        Type type = new TypeToken<ArrayList<RegionWeatherData>>() {}.getType();
        List<RegionWeatherData> regions = gson.fromJson(json, type);

        if (regions == null) {
            regions = new ArrayList<>();
        }

        regionWeatherDataList.setValue(regions);
    }

    public void addRegion(RegionWeatherData regionWeatherData) {
        List<RegionWeatherData> currentList = new ArrayList<>(regionWeatherDataList.getValue());
        currentList.add(regionWeatherData);
        regionWeatherDataList.setValue(currentList);
        saveRegions(currentList);
    }

    public void removeRegions(List<RegionWeatherData> regionsToRemove) {
        List<RegionWeatherData> currentList = new ArrayList<>(regionWeatherDataList.getValue());
        currentList.removeAll(regionsToRemove);
        regionWeatherDataList.setValue(currentList);
        saveRegions(currentList);
    }

    private void saveRegions(List<RegionWeatherData> regions) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(regions);
        editor.putString("region_list", json);
        editor.apply();
    }
}

