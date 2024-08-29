package com.example.weatherui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class regionManagementActivity extends AppCompatActivity {

    private RegionViewModel regionViewModel;
    private WeatherAdapter weatherAdapter;
    private Button deleteButton;
    private boolean isDeleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_management);

        regionViewModel = new ViewModelProvider(this).get(RegionViewModel.class);

        setupUI();
        setupRecyclerView();
        setupObservers();
    }

    private void setupUI() {
        ImageView buttonBack = findViewById(R.id.iv_regionManagement_backarrow);
        buttonBack.setOnClickListener(view -> navigateToMainActivity());

        ImageView optionsButton = findViewById(R.id.iv_regionManagement_optionsButton);
        optionsButton.setOnClickListener(this::showPopupMenu);

        deleteButton = findViewById(R.id.btn_Region_deleteButton);
        deleteButton.setOnClickListener(view -> deleteSelectedRegions());
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(new ArrayList<>());
        recyclerView.setAdapter(weatherAdapter);
    }

    private void setupObservers() {
        regionViewModel.getRegionWeatherDataList().observe(this, regionWeatherData -> {
            weatherAdapter.updateWeatherItems(regionWeatherData);
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                toggleDeleteMode(true);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void toggleDeleteMode(boolean enable) {
        isDeleteMode = enable;
        weatherAdapter.setDeleteMode(enable);
        deleteButton.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    private void deleteSelectedRegions() {
        List<RegionWeatherData> selectedRegions = new ArrayList<>();
        for (RegionWeatherData regionWeatherData : weatherAdapter.getRegionWeatherDataList()) {
            if (regionWeatherData.isSelected()) {
                selectedRegions.add(regionWeatherData);
            }
        }

        regionViewModel.removeRegions(selectedRegions);

        Log.d("DeleteRegion", "Selected regions removed. Total remaining regions: " + weatherAdapter.getRegionWeatherDataList().size());

        toggleDeleteMode(false);
        finish();
    }

    private void navigateToMainActivity() {
        Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backIntent);
    }
}

