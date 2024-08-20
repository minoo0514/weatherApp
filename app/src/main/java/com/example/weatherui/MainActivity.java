package com.example.weatherui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherui.Fragment.ViewPagerAdapter;
import com.example.weatherui.resource.AddEventBottomSheet;
import com.example.weatherui.resource.AddRegionBottomSheet;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    //날씨 API 변수
    private String x = "", y = "";
    private String date = "", time = "";
    private String weatherdata = "";
    private TextView tv_main_weatherCondition, tv_main_tempMax, tv_main_tempNow, tv_main_tempMin;
    private String[] windChillTemp = null;
    private TextView tv_main_windChillTemp;

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
        addButton.setOnClickListener(v -> {
            AddRegionBottomSheet bottomSheet = new AddRegionBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "AddRegionBottomSheet");
        });

        
        viewPager = findViewById(R.id.viewPager);
        tabTemp = findViewById(R.id.tv_main_tabTemp);
        tabRain = findViewById(R.id.tv_main_tabRain);
        tabSchedule = findViewById(R.id.tv_main_tabSch);
        
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

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

        
        
        
        /* new Thread(() -> {
            String getDate = "20240816";
            String getTime = "0800";
            String nx = "60";
            String ny = "127";

            tv_main_weatherCondition = findViewById(R.id.tv_main_weatherCondition);
            tv_main_tempNow = findViewById(R.id.tv_main_tempNow);
            tv_main_tempMax = findViewById(R.id.tv_main_tempMax);
            tv_main_tempMin = findViewById(R.id.tv_main_tempMin);


            WeatherData weatherData = new WeatherData();
            try {
                date = getDate;
                time = getTime;
                x = nx;
                y = ny;
                weatherdata = weatherData.lookWeather(date, time, x, y);
            } catch (IOException e) {
                Log.i("THREE_ERROR1", e.getMessage());
            } catch (JSONException e) {
                Log.i("THREE_ERROR2", e.getMessage());
            }

            runOnUiThread(() -> {
                // return한 값을 " " 기준으로 자른 후 배열에 추가
                // array[0] = 날씨 텍스트, array[1] = 강수량, array[2] = 강수 확률 , array[3] = 기온, array[4] = 적설량 , array[5] = 습도, array[6] = 최고 온도, array[7] = 최저 온도
                String[] weatherarray= weatherdata.split(" ");
                for (int i = 0; i < weatherarray.length; i++) {
                    Log.d("weather = ", i + " " + weatherarray[i]);
                }
                tv_main_weatherCondition.setText(weatherarray[0]);

                tv_main_tempNow.setText(weatherarray[3]);
                tv_main_tempMax.setText(weatherarray[6]);
                tv_main_tempMin.setText(weatherarray[7]);
            });
        }).start();*/

        
        
        /* new Thread(() -> {
            String date = "20240816";

            View otherLayout = getLayoutInflater().inflate(R.layout.fragment_temp, null);
            tv_main_windChillTemp = otherLayout.findViewById(R.id.tv_main_windChillTemp);

            WeatherTemp weatherTemp = new WeatherTemp();
            try {
                windChillTemp = weatherTemp.lookWeatherTemp(date);
            } catch (JSONException e) {
                Log.i("THREE_ERROR3", e.getMessage());
            } catch (IOException e) {
                Log.i("THREE_ERROR4", e.getMessage());
            }

            runOnUiThread(() -> {
                if (windChillTemp != null && windChillTemp.length > 0 && windChillTemp[2] != null) {
                    String formattedText = getString(R.string.windChill_temp, windChillTemp[2]);
                    tv_main_windChillTemp.setText(formattedText);
                } else {
                    tv_main_windChillTemp.setText("데이터 없음");
                }

            });

        }).start();*/
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