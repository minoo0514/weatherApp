package com.example.weatherui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherui.Location.GetCurrentLocation;
import com.example.weatherui.MainFragment.ViewPagerAdapter;
import com.example.weatherui.AddFragment.AddRegionBottomSheet;
import com.example.weatherui.kakaoapi.KakaoApiInterface;
import com.example.weatherui.kakaoapi.KakaoRetrofitInstance;
import com.example.weatherui.kakaoapi.KakaoViewModel;
import com.example.weatherui.kakaoapi.KakaoViewModelFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String MyServiceKey = "E9YTwJF5HtPr5xipNzQvR1AaxrTXHsiPR9TBJAYYlINbSj0XzJZAZkEhfSZXaTQB8v8JWgXbazVcEFK72vAXMw==";
    private RegionViewModel regionViewModel;
    private KakaoViewModel kakaoViewModel;
    private WeatherAdapter weatherAdapter;
    private TextView tv_main_RegionName;
    private RecyclerView weatherRecyclerView;
    private ViewPager2 viewPager;
    private TextView tabTemp, tabRain, tabSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regionViewModel = new ViewModelProvider(this).get(RegionViewModel.class);

        KakaoApiInterface kakaoApiService = KakaoRetrofitInstance.getRetrofitInstance().create(KakaoApiInterface.class);
        KakaoViewModelFactory factory = new KakaoViewModelFactory(kakaoApiService);
        kakaoViewModel = new ViewModelProvider(this, factory).get(KakaoViewModel.class);

        initUI();
        setupRecyclerView();
        setupObservers();
        fetchLocationAndRegionName();  // 위치 기반으로 지역명 가져오는 기능 추가
    }

    private void initUI() {
        Window topWindow = getWindow();
        int appBackgroundColor = ContextCompat.getColor(this, R.color.light_blue);
        topWindow.setStatusBarColor(appBackgroundColor);

        Window bottomWindow = getWindow();
        int bottomWindowColor = ContextCompat.getColor(this, R.color.bottomWindowColor);
        bottomWindow.setNavigationBarColor(bottomWindowColor);

        final DrawerLayout drawerLayout = findViewById(R.id.dl_main_forNaviLeft);
        findViewById(R.id.iv_main_menuIc).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.nv_main_leftMenu);
        navigationView.setItemIconTintList(null);

        Button button_goto = findViewById(R.id.goto_region_management);
        button_goto.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, regionManagementActivity.class);
            startActivity(myIntent);
            finish();
        });

        ImageView addButton = findViewById(R.id.iv_main_addRegion);
        addButton.setOnClickListener(v -> {
            AddRegionBottomSheet bottomSheet = new AddRegionBottomSheet(regionData -> {
                Log.d("onRegionSelected", "onRegionSelected: ");
                regionViewModel.addRegion(new RegionWeatherData(regionData.getName(), null));
            });
            bottomSheet.show(getSupportFragmentManager(), "AddRegionBottomSheet");
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

        tv_main_RegionName = findViewById(R.id.tv_main_RegionName);
    }

    private void setupRecyclerView() {
        weatherRecyclerView = findViewById(R.id.rv_menu_recyclerview);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(new ArrayList<>());
        weatherRecyclerView.setAdapter(weatherAdapter);
    }

    private void setupObservers() {
        regionViewModel.getRegionWeatherDataList().observe(this, regionWeatherData -> {
            // RecyclerView의 어댑터에 데이터 업데이트
            weatherAdapter.updateWeatherItems(regionWeatherData);
        });

        // KakaoViewModel의 region3DepthName을 관찰하여 tv_main_RegionName에 표시
        kakaoViewModel.getRegion3DepthName().observe(this, regionName -> {
            Log.d("KakaoViewModel", "Region name updated: " + regionName);
            tv_main_RegionName.setText(regionName);
        });
    }

    private void fetchLocationAndRegionName() {
        GetCurrentLocation getLocation = new GetCurrentLocation(this);
        getLocation.fetchLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d("MainActivity", "Latitude: " + latitude + ", Longitude: " + longitude);

                    // Kakao API를 사용하여 현재 위치 기반으로 지역명 가져오기
                    kakaoViewModel.fetchRegionName(longitude, latitude);
                } else {
                    tv_main_RegionName.setText("위치를 가져올 수 없습니다.");
                }
            }
        });
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
}

