package com.example.weatherui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class regionManagementActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private List<RegionWeatherData> regionWeatherDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_management);

        ImageView buttonBack = findViewById(R.id.iv_regionManagement_backarrow);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(backIntent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadWeatherData();

        weatherAdapter = new WeatherAdapter(regionWeatherDataList);
        recyclerView.setAdapter(weatherAdapter);
    }

    private void loadWeatherData() {
        SharedPreferences sharedPreferences = getSharedPreferences("regions", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("region_list", null);
        Type type = new TypeToken<ArrayList<RegionWeatherData>>() {}.getType();
        regionWeatherDataList = gson.fromJson(json, type);

        if (regionWeatherDataList == null) {
            regionWeatherDataList = new ArrayList<>();
        }

        Log.d("loadWeatherData", "Loaded regions: " + regionWeatherDataList.size());
    }
}
