package com.example.weatherui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.weatherui.api.MainActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window topWindow = getWindow();
        int appBackgroundColor = ContextCompat.getColor(this, R.color.light_blue); // 중간 앱의 배경색 가져오기
        topWindow.setStatusBarColor(appBackgroundColor);

        Window bottomWindow = getWindow();
        int bottomWindowColor = ContextCompat.getColor(this, R.color.bottomWindowColor);
        bottomWindow.setNavigationBarColor(bottomWindowColor);

        // 일정 시간 지연 이후 실행하기 위한 코드
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 일정 시간이 지나면 MainActivity로 이동
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // 이전 키를 눌렀을 때 스플래시 스크린 화면으로 이동을 방지하기 위해
                // 이동한 다음 사용안함으로 finish 처리
                finish();
            }
        }, 1000); // 시간 2초 이후 실행
    }
}

