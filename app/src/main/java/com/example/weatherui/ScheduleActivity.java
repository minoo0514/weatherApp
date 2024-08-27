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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();
    private boolean isDeleteMode = false;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);

        ImageView buttonBack = findViewById(R.id.iv_schedule_backarrow);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(backIntent);
            }
        });

        ImageView optionsButton = findViewById(R.id.iv_schedule_optionsButton);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        deleteButton = findViewById(R.id.btn_schedule_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedEvents();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rv_scheduleActivity_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        loadEvents();
    }

    private void loadEvents() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("events", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        Gson gson = new Gson();
        Type eventType = new TypeToken<Event>() {}.getType();

        eventList.clear();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String json = (String) entry.getValue();
            Event event = gson.fromJson(json, eventType);
            eventList.add(event);
            Log.d("LoadEvent", "Loaded event: " + entry.getKey() + " -> " + json);
        }

        Log.d("LoadEvent", "Total events loaded: " + eventList.size());
        eventAdapter.notifyDataSetChanged();
    }


    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_add) {
                    return true;
                } else if (itemId == R.id.menu_delete) {
                    toggleDeleteMode(true);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    private void deleteSelectedEvents() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("events", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<Event> selectedEvents = new ArrayList<>();
        for (Event event : eventList) {
            if (event.isSelected()) {
                selectedEvents.add(event);

                // 정확한 키를 찾아서 삭제
                for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
                    String json = (String) entry.getValue();
                    Event storedEvent = new Gson().fromJson(json, Event.class);

                    if (storedEvent.getTitle().equals(event.getTitle()) && storedEvent.getDate().equals(event.getDate())) {
                        editor.remove(entry.getKey());
                        Log.d("DeleteEvent", "Removing event with key: " + entry.getKey());
                        break;
                    }
                }
            }
        }

        editor.apply(); // 데이터 즉시 반영

        eventList.removeAll(selectedEvents);
        eventAdapter.notifyDataSetChanged();

        Log.d("DeleteEvent", "Selected events removed. Total remaining events: " + eventList.size());

        toggleDeleteMode(false); // 삭제 모드 해제
    }

    // 삭제 모드를 관리하는 메서드
    private void toggleDeleteMode(boolean enable) {
        isDeleteMode = enable;
        eventAdapter.setDeleteMode(enable);
        if (enable) {
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.GONE);
        }
    }
}