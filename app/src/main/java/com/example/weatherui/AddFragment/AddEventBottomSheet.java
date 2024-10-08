package com.example.weatherui.AddFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.weatherui.Event;
import com.example.weatherui.R;
import com.example.weatherui.RegionAdapter;
import com.example.weatherui.RegionData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddEventBottomSheet extends BottomSheetDialogFragment {

    private EditText titleEditText;
    private EditText dateEditText;
    private Switch alertSwitch;
    private LinearLayout alertDetailsLayout;
    private Switch temperatureSwitch;
    private Switch rainProbabilitySwitch;
    private Switch rainAmountSwitch;
    private Button saveButton;

    // 지역 추가 변수
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RegionAdapter regionAdapter;
    private List<RegionData> regionList;
    private List<RegionData> filteredList;
    private RegionData selectedRegion; // 선택된 지역 저장


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        // UI 요소 초기화
        titleEditText = view.findViewById(R.id.editTextTitle);
        dateEditText = view.findViewById(R.id.date_edit_text);
        alertSwitch = view.findViewById(R.id.alert_switch);
        alertDetailsLayout = view.findViewById(R.id.alert_details_layout);
        temperatureSwitch = view.findViewById(R.id.temperature_alert_switch);
        rainProbabilitySwitch = view.findViewById(R.id.rain_probability_alert_switch);
        rainAmountSwitch = view.findViewById(R.id.rain_amount_alert_switch);
        saveButton = view.findViewById(R.id.save_button);

        searchView = view.findViewById(R.id.sv_addEvent_addRegion);
        recyclerView = view.findViewById(R.id.rv_addEvent_regionList);

        regionList = getRegionList(); // 모든 지역 리스트 가져오기
        filteredList = new ArrayList<>(); // 필터링된 리스트 초기화
        regionAdapter = new RegionAdapter(filteredList, new RegionAdapter.OnRegionClickListener() {
            @Override
            public void onRegionClick(RegionData region) {
                selectedRegion = region;  // 선택된 지역 저장
                recyclerView.setVisibility(View.GONE);  // RecyclerView를 숨김
                searchView.setQuery(region.getName(), false);  // 선택된 지역을 SearchView에 표시
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(regionAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRegionList(newText);  // 사용자가 텍스트를 입력할 때마다 리스트를 필터링
                return true;
            }
        });


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        // 알림 스위치의 상태 변경 리스너
        alertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alertDetailsLayout.setVisibility(View.VISIBLE);
                } else {
                    alertDetailsLayout.setVisibility(View.GONE);
                    temperatureSwitch.setChecked(false);
                    rainProbabilitySwitch.setChecked(false);
                    rainAmountSwitch.setChecked(false);
                }
            }
        });

        // 저장 버튼 클릭 리스너
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String date = dateEditText.getText().toString();
                boolean isAlertEnabled = alertSwitch.isChecked();
                boolean isTemperatureAlert = temperatureSwitch.isChecked();
                boolean isRainProbabilityAlert = rainProbabilitySwitch.isChecked();
                boolean isRainAmountAlert = rainAmountSwitch.isChecked();

                // 데이터를 SharedPreferences에 저장
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("events", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                Gson gson = new Gson();
                Event event = new Event(title, date, isAlertEnabled, isTemperatureAlert, isRainProbabilityAlert, isRainAmountAlert,  selectedRegion);
                String json = gson.toJson(event);
                editor.putString("event_" + System.currentTimeMillis(), json);
                editor.apply();

                Log.d("SaveEvent", "Event Saved: " + json);

                dismiss(); // BottomSheetDialogFragment 닫기
            }
        });

        return view;
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // 날짜 선택 다이얼로그
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // 선택된 날짜를 yyyymmdd 형식으로 포맷
                final String selectedDate = String.format("%04d%02d%02d", selectedYear, selectedMonth + 1, selectedDay);

                // 시간 선택 다이얼로그
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        // 시간은 HHmm 형식으로 포맷 (분은 00으로 고정)
                        String selectedTime = String.format("%02d00", selectedHour);
                        String formattedDateTime = selectedDate + " " + selectedTime;

                        // 사용자에게 표시할 포맷 생성
                        String displayDateTime = convertToDisplayFormat(selectedYear, selectedMonth, selectedDay, selectedHour);
                        dateEditText.setText(displayDateTime);
                        dateEditText.setTag(formattedDateTime);  // 실제 저장할 값은 태그에 저장
                    }
                }, hour, 0, true);  // 분은 00으로 고정
                timePickerDialog.show();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    // 실제 날짜 및 시간을 표시할 형식으로 변환
    private String convertToDisplayFormat(int year, int month, int day, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, 0);

        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시", Locale.getDefault());
        return displayFormat.format(calendar.getTime());
    }

    private void filterRegionList(String query) {
        filteredList.clear();
        if (!query.isEmpty()) {
            for (RegionData region : regionList) {
                Log.d("FilterCheck", "Checking region: " + region.getName()); // 지역 이름 확인
                Log.d("FilterCheck", "Query: " + query);// 지역 이름 확인
                if (region.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(region);
                    Log.d("RegionAdapter", "Binding region: " + region.getName());
                }
            }
        }
        Log.d("RegionAdapter", "FilteredList size: " + filteredList.size());


        regionAdapter.updateList(filteredList);
    }

    private List<RegionData> getRegionList() {
        List<RegionData> regionList = new ArrayList<>();
        regionList.add(new RegionData("서울특별시", "1100000000", "60", "127"));
        regionList.add(new RegionData("부산광역시", "2600000000", "98", "76"));
        regionList.add(new RegionData("대구광역시", "2700000000", "89", "90"));
        regionList.add(new RegionData("인천광역시", "2800000000", "55", "124"));
        regionList.add(new RegionData("광주광역시", "2900000000", "58", "74"));
        regionList.add(new RegionData("울산광역시", "3100000000", "102", "84"));
        regionList.add(new RegionData("대전광역시", "3000000000", "67", "100"));
        return regionList;
    }
}

