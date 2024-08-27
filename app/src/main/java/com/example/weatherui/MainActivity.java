package com.example.weatherui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherui.Location.GetCurrentLocation;
import com.example.weatherui.Location.GetCurrentTime;
import com.example.weatherui.MainFragment.ViewPagerAdapter;
import com.example.weatherui.AddFragment.AddRegionBottomSheet;
import com.example.weatherui.api.RetrofitInstance;
import com.example.weatherui.api.WeatherApiInterface;
import com.example.weatherui.api.WeatherViewModel;
import com.example.weatherui.kakaoapi.KakaoApiInterface;
import com.example.weatherui.kakaoapi.KakaoViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // api 변수
    private final String MyServiceKey = "E9YTwJF5HtPr5xipNzQvR1AaxrTXHsiPR9TBJAYYlINbSj0XzJZAZkEhfSZXaTQB8v8JWgXbazVcEFK72vAXMw==";
    private String TodayTime = "0800";

    private KakaoViewModel kakaoViewModel;
    private TextView tv_main_RegionName;

    // 메뉴에 recyclerview 변수
    private RecyclerView weatherRecyclerView;
    private WeatherViewModel weatherViewModel;
    private WeatherAdapter weatherAdapter;

    //ViewPager 변수
    private ViewPager2 viewPager;
    private TextView tabTemp, tabRain, tabSchedule;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Window topWindow = getWindow();
        int appBackgroundColor = ContextCompat.getColor(this, R.color.light_blue);
        topWindow.setStatusBarColor(appBackgroundColor);

        Window bottomWindow = getWindow();
        int bottomWindowColor = ContextCompat.getColor(this, R.color.bottomWindowColor);
        bottomWindow.setNavigationBarColor(bottomWindowColor);

        final DrawerLayout drawerLayout = findViewById(R.id.dl_main_forNaviLeft);
        findViewById(R.id.iv_main_menuIc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.nv_main_leftMenu);
        navigationView.setItemIconTintList(null);

        
        
        Button button_goto = findViewById(R.id.goto_region_management);
        button_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, regionManagementActivity.class);
                startActivity(myIntent);
                finish();
            }
        });



        ImageView addButton = findViewById(R.id.iv_main_addRegion);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRegionBottomSheet bottomSheet = new AddRegionBottomSheet(new AddRegionBottomSheet.OnRegionSelectedListener() {
                    @Override
                    public void onRegionSelected(RegionData regionData) {
                        Log.d("onRegionSelected", "onRegionSelected: ");
                        saveSelectedRegion(regionData); // 선택된 지역을 저장
                        requestWeatherDataForRegion(regionData);
                    }
                });
                bottomSheet.show(getSupportFragmentManager(), "AddRegionBottomSheet");
            }
        });


        ImageView addMenuButton = findViewById(R.id.im_navDrawer_addRegion);
        addMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRegionBottomSheet bottomSheet = new AddRegionBottomSheet(new AddRegionBottomSheet.OnRegionSelectedListener() {
                    @Override
                    public void onRegionSelected(RegionData regionData) {
                        Log.d("onRegionSelected", "onRegionSelected: ");
                        saveSelectedRegion(regionData); // 선택된 지역을 저장
                        requestWeatherDataForRegion(regionData);
                    }
                });
                bottomSheet.show(getSupportFragmentManager(), "AddRegionBottomSheet");
            }
        });

        
        viewPager = findViewById(R.id.viewPager);
        tabTemp = findViewById(R.id.tv_main_tabTemp);
        tabRain = findViewById(R.id.tv_main_tabRain);
        tabSchedule = findViewById(R.id.tv_main_tabSch);
        
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(2);

        updateTabs(0);

        tabTemp.setOnClickListener(v -> viewPager.setCurrentItem(0));
        tabRain.setOnClickListener(v -> viewPager.setCurrentItem(1));
        tabSchedule.setOnClickListener(v -> viewPager.setCurrentItem(2));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateTabs(position);
            }
        });


        weatherRecyclerView = findViewById(R.id.rv_menu_recyclerview);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(new ArrayList<>());
        weatherRecyclerView.setAdapter(weatherAdapter);

        // ViewModel 설정
        WeatherApiInterface apiService = RetrofitInstance.getWeatherRetrofitInstance().create(WeatherApiInterface.class);
        weatherViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                WeatherApiInterface apiService = RetrofitInstance.getWeatherRetrofitInstance().create(WeatherApiInterface.class);
                return (T) new WeatherViewModel(apiService);
            }
        }).get(WeatherViewModel.class);

        weatherViewModel.getRegionWeatherItems().observe(this, new Observer<List<RegionWeatherData>>() {
            @Override
            public void onChanged(List<RegionWeatherData> items) {
                weatherAdapter.updateWeatherItems(items);
            }
        });

        KakaoApiInterface kakaoApiService = RetrofitInstance.getKakaoRetrofitInstance().create(KakaoApiInterface.class);
        kakaoViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new KakaoViewModel(kakaoApiService);
            }
        }).get(KakaoViewModel.class);

        tv_main_RegionName = findViewById(R.id.tv_main_RegionName);
        kakaoViewModel.getRegion3DepthName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String regionName) {
                tv_main_RegionName.setText(regionName);
            }
        });

        fetchLocationAndRegionName();

        ImageView im_navDrawer_addRegion = findViewById(R.id.im_navDrawer_addRegion);
        im_navDrawer_addRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRegionBottomSheet bottomSheet = new AddRegionBottomSheet(new AddRegionBottomSheet.OnRegionSelectedListener() {
                    @Override
                    public void onRegionSelected(RegionData regionData) {
                        Log.d("onRegionSelected", "onRegionSelected: ");
                        saveSelectedRegion(regionData);
                        requestWeatherDataForRegion(regionData);
                    }
                });
                bottomSheet.show(getSupportFragmentManager(), "AddRegionBottomSheet");
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("regions", MODE_PRIVATE);
        String regionName = sharedPreferences.getString("region_name", null);
        String nx = sharedPreferences.getString("nx", null);
        String ny = sharedPreferences.getString("ny", null);

        if (regionName != null && nx != null && ny != null) {
            // 저장된 지역 정보로 WeatherData 요청
            RegionData savedRegion = new RegionData(regionName, "", nx, ny);
            requestWeatherDataForRegion(savedRegion);
        }
    }

    private void updateTabs(int position) {
        tabTemp.setBackground(null);
        tabRain.setBackground(null);
        tabSchedule.setBackground(null);

        if (position == 0) {
            tabTemp.setBackgroundResource(R.drawable.selected_tab_background);
        } else if (position == 1) {
            tabRain.setBackgroundResource(R.drawable.selected_tab_background);
        } else if (position == 2) {
            tabSchedule.setBackgroundResource(R.drawable.selected_tab_background);
        }
    }

    private void saveSelectedRegion(RegionData regionData) {
        // SharedPreferences 초기화
        SharedPreferences sharedPreferences = getSharedPreferences("regions", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 기존 지역 목록 불러오기
        Gson gson = new Gson();
        String json = sharedPreferences.getString("region_list", null);
        Type type = new TypeToken<ArrayList<RegionData>>() {}.getType();
        List<RegionData> regionList = gson.fromJson(json, type);

        if (regionList == null) {
            regionList = new ArrayList<>();
        }

        // 새 지역 추가
        regionList.add(regionData);

        // 업데이트된 리스트 저장
        String updatedJson = gson.toJson(regionList);
        editor.putString("region_list", updatedJson);
        editor.apply();
        Log.d("saveSelectedRegion", "Saved regions: " + updatedJson);
    }

    private void requestWeatherDataForRegion(RegionData regionData) {
        if (regionData == null || regionData.getName() == null || regionData.getNx() == null || regionData.getNy() == null) {
            Log.e("requestWeatherDataForRegion", "Invalid RegionData");
            return;
        }

        GetCurrentTime getTime = new GetCurrentTime();
        String currentDate = getTime.getCurrentDate();
        String currentTime = getTime.getCurrentTime();
        String RegionName = regionData.getName();
        String Nx = regionData.getNx();
        String Ny = regionData.getNy();

        weatherViewModel.fetchRegionWeatherData(RegionName, MyServiceKey, 100, 1, "JSON", currentDate, currentTime, Nx, Ny);
    }

    private void fetchLocationAndRegionName() {
        GetCurrentLocation getLocation = new GetCurrentLocation(this);
        getLocation.fetchLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // 현재 위치에 따른 Kakao API 호출
                    kakaoViewModel.fetchRegionName(longitude, latitude);
                } else {
                    tv_main_RegionName.setText("위치를 가져올 수 없습니다.");
                }
            }
        });
    }
}