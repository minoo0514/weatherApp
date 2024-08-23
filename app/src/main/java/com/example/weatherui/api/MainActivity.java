package com.example.weatherui.api;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherui.R;
import com.example.weatherui.MainFragment.ViewPagerAdapter;
import com.example.weatherui.AddFragment.AddRegionBottomSheet;
import com.example.weatherui.regionManagementActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    //ViewPager 변수
    private ViewPager2 viewPager;
    private TextView tabTemp, tabRain, tabSchedule;

    //View
    private TextView tv_main_tempNow;
    
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
                AddRegionBottomSheet bottomSheet = new AddRegionBottomSheet();
                bottomSheet.show(getSupportFragmentManager(), "AddRegionBottomSheet");
            }
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