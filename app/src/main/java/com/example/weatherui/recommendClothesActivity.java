package com.example.weatherui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class recommendClothesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommend_clothes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        BottomNavigationView bottomNavigationViewClothes = findViewById(R.id.bottomNavigationView_clothes);
        bottomNavigationViewClothes.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return navigateToFragment(item.getItemId());
            }
        });

        if (savedInstanceState == null) {
            bottomNavigationViewClothes.setSelectedItemId(R.id.action_today); // Default fragment at launch
        }

        ImageView buttonBackClothes = findViewById(R.id.management_back_clothes);
        buttonBackClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(backIntent);
            }
        });
    }

    private boolean navigateToFragment(int itemId) {
        Fragment selectedFragment = null;
        if (itemId == R.id.action_today) {
            selectedFragment = new todayFragment();
        } else if (itemId == R.id.action_tomorrow) {
            selectedFragment = new tomorrowFragment();
        } else if (itemId == R.id.action_dayAfterTomorrow) {
            selectedFragment = new dayAfterTomorrowFragment();
        } else if (itemId == R.id.action_threedaysLater) {
            selectedFragment = new dayAfterTomorrowFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_clothes, selectedFragment)
                    .commit();
            return true;
        }
        return false;
    }
}