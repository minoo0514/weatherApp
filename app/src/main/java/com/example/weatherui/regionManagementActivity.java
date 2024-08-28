package com.example.weatherui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherui.Location.GetCurrentTime;
import com.example.weatherui.api.WeatherApiInterface;
import com.example.weatherui.api.WeatherViewModel;
import com.example.weatherui.api.RetrofitInstance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class regionManagementActivity extends AppCompatActivity {

    private static final String MY_SERVICE_KEY = "E9YTwJF5HtPr5xipNzQvR1AaxrTXHsiPR9TBJAYYlINbSj0XzJZAZkEhfSZXaTQB8v8JWgXbazVcEFK72vAXMw==";

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private WeatherViewModel weatherViewModel;
    private List<RegionWeatherData> regionWeatherDataList = new ArrayList<>();
    private Button deleteButton;
    private boolean isDeleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_management);

        setupUI();

        initViewModelAndRecyclerView();

        loadWeatherData();
    }

    private void setupUI() {
        ImageView buttonBack = findViewById(R.id.iv_regionManagement_backarrow);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToMainActivity();
            }
        });

        ImageView optionsButton = findViewById(R.id.iv_regionManagement_optionsButton);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        deleteButton = findViewById(R.id.btn_Region_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedRegions();
            }
        });
    }

    private void initViewModelAndRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(new ArrayList<RegionWeatherData>());
        recyclerView.setAdapter(weatherAdapter);

        weatherViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                WeatherApiInterface apiService = RetrofitInstance.getWeatherRetrofitInstance().create(WeatherApiInterface.class);
                return (T) new WeatherViewModel(apiService);
            }
        }).get(WeatherViewModel.class);
    }

    private void loadWeatherData() {
        List<RegionData> savedRegionList = getSavedRegionList();

        if (savedRegionList != null) {
            for (RegionData region : savedRegionList) {
                requestWeatherDataForRegion(region);
            }
        }
    }

    private List<RegionData> getSavedRegionList() {
        SharedPreferences sharedPreferences = getSharedPreferences("regions", MODE_PRIVATE);
        String json = sharedPreferences.getString("region_list", null);
        Type type = new TypeToken<ArrayList<RegionData>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    public void requestWeatherDataForRegion(RegionData regionData) {
        if (regionData == null || regionData.getName() == null || regionData.getNx() == null || regionData.getNy() == null) {
            Log.e("requestWeatherDataForRegion", "Invalid RegionData");
            return;
        }

        Log.d("requestWeatherDataForRegion", "Requesting weather data for region: "
                + regionData.getName()
                + " (Nx: " + regionData.getNx()
                + ", Ny: " + regionData.getNy() + ")");

        GetCurrentTime getTime = new GetCurrentTime();
        String currentDate = getTime.getCurrentDate();
        String currentTime = getTime.getCurrentTime();
        String regionName = regionData.getName();
        String nx = regionData.getNx();
        String ny = regionData.getNy();

        weatherViewModel.fetchRegionWeatherData(regionName, MY_SERVICE_KEY, 100, 1, "JSON", currentDate, currentTime, nx, ny);

        weatherViewModel.getRegionWeatherItems().observe(this, new Observer<List<RegionWeatherData>>() {
            @Override
            public void onChanged(List<RegionWeatherData> items) {
                Log.d("requestWeatherDataForRegion", "API response for region: " + regionData.getName()
                        + ", Items received: " + (items != null ? items.size() : "null"));
                weatherAdapter.updateWeatherItems(items);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_delete) {
                    toggleDeleteMode(true);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void toggleDeleteMode(boolean enable) {
        isDeleteMode = enable;
        weatherAdapter.setDeleteMode(enable);
        deleteButton.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    private void deleteSelectedRegions() {
        List<RegionData> savedRegionList = getSavedRegionList();
        if (savedRegionList == null) {
            savedRegionList = new ArrayList<>();
        }

        List<RegionWeatherData> selectedRegions = new ArrayList<>();
        for (RegionWeatherData regionWeatherData : regionWeatherDataList) {
            if (regionWeatherData.isSelected()) {
                selectedRegions.add(regionWeatherData);
                removeRegionFromList(savedRegionList, regionWeatherData);
            }
        }

        saveUpdatedRegionList(savedRegionList);
        regionWeatherDataList.removeAll(selectedRegions);
        weatherAdapter.notifyDataSetChanged();

        Log.d("DeleteRegion", "Selected regions removed. Total remaining regions: " + regionWeatherDataList.size());
        finish(); // Finish the activity to notify MainActivity about the changes
    }

    private void removeRegionFromList(List<RegionData> savedRegionList, RegionWeatherData regionWeatherData) {
        for (int i = 0; i < savedRegionList.size(); i++) {
            RegionData regionData = savedRegionList.get(i);
            if (regionData.getName().equals(regionWeatherData.getRegionName())) {
                savedRegionList.remove(i);
                Log.d("DeleteRegion", "Removing region: " + regionWeatherData.getRegionName());
                break;
            }
        }
    }

    private void saveUpdatedRegionList(List<RegionData> savedRegionList) {
        SharedPreferences sharedPreferences = getSharedPreferences("regions", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String updatedJson = new Gson().toJson(savedRegionList);
        editor.putString("region_list", updatedJson);
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backIntent);
    }
}


