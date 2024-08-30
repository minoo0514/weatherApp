package com.example.weatherui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherui.Location.ConvertLocation;
import com.example.weatherui.WeatherApi.WeatherBitApiInterface;
import com.example.weatherui.WeatherApi.WeatherBitData;
import com.example.weatherui.WeatherApi.WeatherBitRepository;
import com.example.weatherui.WeatherApi.WeatherBitRetrofitInstance;
import com.example.weatherui.WeatherApi.WeatherBitViewModel;
import com.example.weatherui.WeatherApi.WeatherBitViewModelFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();
    private WeatherBitViewModel weatherBitViewModel;
    private final String WEATHERBIT_API_KEY = "7ccd4cb73f7948088d46b23d9b41ce3b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);

        // ViewModel 초기화
        WeatherBitRepository repository = new WeatherBitRepository(
                WeatherBitRetrofitInstance.getWeatherRetrofitInstance().create(WeatherBitApiInterface.class)
        );
        WeatherBitViewModelFactory factory = new WeatherBitViewModelFactory(repository);
        weatherBitViewModel = new ViewModelProvider(this, factory).get(WeatherBitViewModel.class);

        setupUI();
        setupRecyclerView();
        loadEvents();

        // 날씨 데이터를 가져오는 로직
        fetchWeatherForEvents();
    }

    private void setupUI() {
        ImageView buttonBack = findViewById(R.id.iv_schedule_backarrow);
        buttonBack.setOnClickListener(view -> navigateToMainActivity());

        ImageView optionsButton = findViewById(R.id.iv_schedule_optionsButton);
        optionsButton.setOnClickListener(this::showPopupMenu);

        Button deleteButton = findViewById(R.id.btn_schedule_deleteButton);
        deleteButton.setOnClickListener(v -> deleteSelectedEvents());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.rv_schedule_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);
    }

    private void loadEvents() {
        SharedPreferences sharedPreferences = getSharedPreferences("events", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        Gson gson = new Gson();
        Type eventType = new TypeToken<Event>() {}.getType();

        eventList.clear();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String json = (String) entry.getValue();
            Event event = gson.fromJson(json, eventType);
            eventList.add(event);
        }
        eventAdapter.notifyDataSetChanged();
    }

    private void fetchWeatherForEvents() {
        for (Event event : eventList) {
            ConvertLocation.LatLon latLon = ConvertLocation.convertToLatLon(
                    Integer.parseInt(event.getRegion().getNx()),
                    Integer.parseInt(event.getRegion().getNy())
            );
            weatherBitViewModel.fetchWeatherByLatLon(
                    String.valueOf(latLon.lat),
                    String.valueOf(latLon.lon),
                    WEATHERBIT_API_KEY
            );
        }

        weatherBitViewModel.getWeatherData().observe(this, weatherData -> {
            if (weatherData != null && !weatherData.getData().isEmpty()) {
                updateWeatherInEvents(weatherData);
            }
        });
    }

    private void updateWeatherInEvents(WeatherBitData weatherData) {
        for (Event event : eventList) {
            for (WeatherBitData.DailyForecast forecast : weatherData.getData()) {
                String formattedValidDate = forecast.getValidDate().replace("-", "");
                String eventDateFormatted = convertDateToYYYYMMDD(event.getDate());

                if (formattedValidDate.equals(eventDateFormatted)) {
                    event.setTemperature(forecast.getTemperature());
                    event.setPrecipitationAmount(forecast.getPrecipitationAmount());
                    event.setPrecipitationProbability(forecast.getPrecipitationProbability());
                    event.setCloudCoverage(forecast.getCloudCoverage());
                    break;
                }
            }
        }
        eventAdapter.notifyDataSetChanged();
    }

    private String convertDateToYYYYMMDD(String eventDate) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시", Locale.KOREA);
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        try {
            Date date = originalFormat.parse(eventDate);
            return targetFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void navigateToMainActivity() {
        Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backIntent);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_add) {
                return true;
            } else if (item.getItemId() == R.id.menu_delete) {
                toggleDeleteMode(true);
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    private void deleteSelectedEvents() {
        SharedPreferences sharedPreferences = getSharedPreferences("events", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<Event> selectedEvents = new ArrayList<>();
        for (Event event : eventList) {
            if (event.isSelected()) {
                selectedEvents.add(event);

                for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
                    String json = (String) entry.getValue();
                    Event storedEvent = new Gson().fromJson(json, Event.class);

                    if (storedEvent.getTitle().equals(event.getTitle()) && storedEvent.getDate().equals(event.getDate())) {
                        editor.remove(entry.getKey());
                        break;
                    }
                }
            }
        }

        editor.apply();
        eventList.removeAll(selectedEvents);
        eventAdapter.notifyDataSetChanged();
        toggleDeleteMode(false);
    }

    private void toggleDeleteMode(boolean enable) {
        eventAdapter.setDeleteMode(enable);
        findViewById(R.id.btn_schedule_deleteButton).setVisibility(enable ? View.VISIBLE : View.GONE);
    }
}

