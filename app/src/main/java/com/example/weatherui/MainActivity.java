package com.example.weatherui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout drawerLayout = findViewById(R.id.dl_main_forNaviLeft);
        findViewById(R.id.tv_main_menuIcRegionName).setOnClickListener(new View.OnClickListener() {
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

        Button button_goto_clothes = findViewById(R.id.btn_main_gotoRecommendClothes);
        button_goto_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, recommendClothesActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        ConstraintLayout layoutClickable = findViewById(R.id.cl_main_);

        layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DetailGraphActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_main_byNaviMenu);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return navigateToFragment(item.getItemId());
            }
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.action_temp); // Default fragment at launch
        }
    }

    private boolean navigateToFragment(int itemId) {
        Fragment selectedFragment = null;
        if (itemId == R.id.action_temp) {
            selectedFragment = new TempFragment();
        } else if (itemId == R.id.action_rain) {
            selectedFragment = new RainFragment();
        } else if (itemId == R.id.action_humid) {
            selectedFragment = new HumidFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_main_fragmentContainer, selectedFragment)
                    .commit();
            return true;
        }
        return false;
    }
}