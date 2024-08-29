package com.example.weatherui.MainFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherui.Event;
import com.example.weatherui.EventAdapter;
import com.example.weatherui.Location.ConvertLocation;
import com.example.weatherui.Location.GetCurrentLocation;
import com.example.weatherui.Location.GetCurrentTime;
import com.example.weatherui.R;
import com.example.weatherui.AddFragment.AddEventBottomSheet;
import com.example.weatherui.ScheduleActivity;
import com.example.weatherui.WeatherApi.WeatherBitApiInterface;
import com.example.weatherui.WeatherApi.WeatherBitData;
import com.example.weatherui.WeatherApi.WeatherBitRepository;
import com.example.weatherui.WeatherApi.WeatherBitViewModel;
import com.example.weatherui.api.WeatherRetrofitInstance;
import com.example.weatherui.api.WeatherApiInterface;
import com.example.weatherui.api.WeatherData;
import com.example.weatherui.api.WeatherViewModel;
import com.example.weatherui.api.WindChillData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ScheduleFragment extends Fragment {

    //WeatherBit 변수
    private WeatherBitViewModel weatherBitViewModel;
    private final String WEATHERBIT_API_KEY = "7ccd4cb73f7948088d46b23d9b41ce3b";

    //일정 변수
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();

    private GetCurrentLocation getLocation;
    private GetCurrentTime getTime;

    private TextView tv_main_tempNow;
    private TextView tv_main_weatherCondition;
    private TextView tv_main_tempMax;
    private TextView tv_main_tempMin;
    private TextView tv_main_windChillTemp;
    private WeatherViewModel weatherViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        Button addSchButton = view.findViewById(R.id.btn_ScheduleFragment_addSch);
        addSchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BottomSheetDialogFragment 호출
                AddEventBottomSheet bottomSheet = new AddEventBottomSheet();
                bottomSheet.show(getParentFragmentManager(), "AddEventBottomSheet");
            }
        });


        tv_main_tempNow = view.findViewById(R.id.tv_main_tempNow);
        tv_main_weatherCondition = view.findViewById(R.id.tv_main_weatherCondition);
        tv_main_tempMax = view.findViewById(R.id.tv_main_tempMax);
        tv_main_tempMin = view.findViewById(R.id.tv_main_tempMin);
        tv_main_windChillTemp = view.findViewById(R.id.tv_main_windChillTemp);

        getLocation = new GetCurrentLocation(getActivity());
        getTime = new GetCurrentTime();

        WeatherApiInterface weatherApiInterface = WeatherRetrofitInstance.getWeatherRetrofitInstance().create(WeatherApiInterface.class);

        weatherViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new WeatherViewModel(weatherApiInterface);
            }
        }).get(WeatherViewModel.class);

        fetchCurrentLocationAndWeather();

        // WeatherBitViewModel 초기화
        WeatherBitApiInterface weatherBitApiInterface = WeatherBitApiInterface.getWeatherBitRetrofitInstance().create(WeatherBitApiInterface.class);
        WeatherBitViewModel weatherBitViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new WeatherBitViewModel(new WeatherBitRepository(weatherBitApiInterface));
            }
        }).get(WeatherBitViewModel.class);

        recyclerView = view.findViewById(R.id.rv_schedule_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        loadEvents();

        Button goToScheduleButton = view.findViewById(R.id.btn_tempFragment_goSch);
        goToScheduleButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScheduleActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void fetchCurrentLocationAndWeather() {
        getLocation.fetchLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    Log.d("fetchCurrentLocationAndWeather", "latitude: " + latitude + ", longitude: " + longitude);

                    // 현재 위치의 위도/경도를 격자 좌표로 변환
                    ConvertLocation.GridLocation gridLocation = ConvertLocation.convertToGrid(latitude, longitude);
                    String nx = String.valueOf(gridLocation.nx);
                    String ny = String.valueOf(gridLocation.ny);
                    Log.d("fetchCurrentLocationAndWeather", "nx: " + nx + ", ny: " + ny);

                    // 격자 좌표를 사용하여 API 요청
                    fetchWeatherWithLocation(nx, ny);
                } else {
                    Toast.makeText(getActivity(), "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchWeatherWithLocation(String nx, String ny) {
        String currentDate = getTime.getCurrentDate();
        String currentTime = getTime.getCurrentTime();
        // 현재 위치 기반으로 API 요청
        String WEATHER_API_KEY = "E9YTwJF5HtPr5xipNzQvR1AaxrTXHsiPR9TBJAYYlINbSj0XzJZAZkEhfSZXaTQB8v8JWgXbazVcEFK72vAXMw==";
        weatherViewModel.fetchWeatherData(WEATHER_API_KEY, 100, 1, "JSON", currentDate, currentTime, nx, ny);
        observeWeatherData();

        weatherViewModel.fetchWeatherData(WEATHER_API_KEY, 500, 1, "JSON", currentDate, "0200", nx, ny);
        observeTempMaxMinData();

        String currentDateTime = getCurrentDateTime();
        weatherViewModel.fetchWindChillData(WEATHER_API_KEY, 100, 1, "JSON", "1100000000", currentDateTime, "A41");
        observeWindChillData();
    }

    private void observeWeatherData() {
        Log.d("WeatherFragment", "observeWeatherData: ");
        weatherViewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<WeatherData>() {
            @Override
            public void onChanged(WeatherData weatherData) {
                String currentTime = getTime.getCurrentTime();
                String TemporalTime = getNextHour(currentTime);
                if (weatherData == null) {
                    Log.e("WeatherFragment", "WeatherData is null");
                    tv_main_tempNow.setText("no data");
                    tv_main_weatherCondition.setText("no data");
                    return;
                }

                String Sky = weatherData.getFcstValue("SKY", getTime.getCurrentDate(), TemporalTime);
                if (Objects.equals(Sky, "1")) {
                    tv_main_weatherCondition.setText("맑음");
                } else if (Objects.equals(Sky, "3")) {
                    tv_main_weatherCondition.setText("구름많음");
                } else if (Objects.equals(Sky, "4")) {
                    tv_main_weatherCondition.setText("흐림");
                } else {
                    Log.e("WeatherFragment", "getFcstValue returned null");
                    tv_main_weatherCondition.setText("no data");
                }

                String Temp = weatherData.getFcstValue("TMP", getTime.getCurrentDate(), TemporalTime);
                if (Temp != null) {
                    tv_main_tempNow.setText(Temp + "°");
                } else {
                    Log.e("WeatherFragment", "getFcstValue returned null");
                    tv_main_tempNow.setText("no data");
                }
            }
        });
    }

    private void observeTempMaxMinData() {
        Log.d("WeatherFragment", "observeTempMaxMinData: ");
        weatherViewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<WeatherData>() {
            @Override
            public void onChanged(WeatherData weatherData) {
                if (weatherData == null) {
                    Log.e("WeatherFragment", "WeatherData is null");
                    tv_main_tempMax.setText("no data");
                    tv_main_tempMin.setText("no data");
                    return;
                }

                String TempMax = weatherData.getFcstValueNoTime("TMX", getTime.getCurrentDate());
                if (TempMax != null) {
                    tv_main_tempMax.setText(TempMax + "°");
                } else {
                    Log.e("WeatherFragment", "getFcstValue returned null");
                    tv_main_tempMax.setText("no data");
                }

                String TempMin = weatherData.getFcstValueNoTime("TMN", getTime.getCurrentDate());
                if (TempMin != null) {
                    tv_main_tempMin.setText(TempMin + "°");
                } else {
                    Log.e("WeatherFragment", "getFcstValue returned null");
                    tv_main_tempMin.setText("no data");
                }
            }
        });
    }

    private void observeWindChillData() {
        Log.d("WeatherFragment", "observeWindChillData: ");
        weatherViewModel.getWindChillData().observe(getViewLifecycleOwner(), new Observer<WindChillData>() {
            @Override
            public void onChanged(WindChillData windChillData) {
                if (windChillData == null) {
                    Log.e("WeatherFragment", "WindChillData is null");
                    tv_main_windChillTemp.setText("no data");
                    return;
                }

                WindChillData.Item item = windChillData.response.body.items.itemList.get(0);
                String windChill = item.h1;
                if (windChill != null) {
                    String formattedText = String.format("체감온도 %1$s°", windChill);
                    tv_main_windChillTemp.setText(formattedText);
                } else {
                    Log.e("WeatherFragment", "getWindChill returned null");
                    tv_main_windChillTemp.setText("no data");
                }
            }
        });
    }

    private void loadEvents() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("events", Context.MODE_PRIVATE);
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
        eventAdapter.notifyDataSetChanged(); // 데이터 변경 알림
    }

    // 새롭게 추가된 WeatherBit API 요청 함수
    private void fetchWeatherForEvent(Event event) {
        if (event.getRegion() != null) {
            String lat = event.getRegion().getNx(); // 격자 x를 위도로 변환
            String lon = event.getRegion().getNy(); // 격자 y를 경도로 변환

            weatherBitViewModel.fetchWeatherByLatLon(lat, lon, WEATHERBIT_API_KEY, 1);
            weatherBitViewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<WeatherBitData>() {
                @Override
                public void onChanged(WeatherBitData weatherData) {
                    if (weatherData != null && weatherData.getData() != null && !weatherData.getData().isEmpty()) {
                        WeatherBitData.DailyForecast forecast = weatherData.getData().get(0);
                        event.setWeatherDescription(forecast.getWeather().getDescription());
                        event.setTemperature(forecast.getTemperature());
                        eventAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private String getNextHour(String currentTime) {
        // currentTime을 정수로 변환하여 100을 더함
        int timeInt = Integer.parseInt(currentTime);
        timeInt += 100;

        // 만약 2400을 초과하는 경우, 시간 값을 0000으로 리셋 (24시간 포맷 유지)
        if (timeInt >= 2400) {
            timeInt -= 2400;
        }

        // 다시 문자열 형식으로 변환하고 앞자리가 0으로 시작할 경우, 형식 유지
        return String.format("%04d", timeInt);
    }

    private String getCurrentDateTime() {
        // 현재 시각을 가져와서 yyyyMMddHH 형식으로 포맷
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    @Override
    public void onResume() {
        super.onResume();
        eventList.clear();
        loadEvents();
    }
}