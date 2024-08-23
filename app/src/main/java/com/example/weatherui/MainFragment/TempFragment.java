package com.example.weatherui.MainFragment;

import android.os.Bundle;

import android.location.Location;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherui.Location.ConvertLocation;
import com.example.weatherui.Location.GetCurrentLocation;
import com.example.weatherui.Location.GetCurrentTime;
import com.example.weatherui.R;
import com.example.weatherui.api.RetrofitInstance;
import com.example.weatherui.api.WeatherApiInterface;
import com.example.weatherui.api.WeatherData;
import com.example.weatherui.api.WeatherViewModel;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TempFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TempFragment extends Fragment {

    private GetCurrentLocation  getLocation;
    private GetCurrentTime getTime;

    private TextView tv_main_tempNow;
    private WeatherViewModel weatherViewModel;
    private final String MyServiceKey = "E9YTwJF5HtPr5xipNzQvR1AaxrTXHsiPR9TBJAYYlINbSj0XzJZAZkEhfSZXaTQB8v8JWgXbazVcEFK72vAXMw==";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TempFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TempFragment newInstance(String param1, String param2) {
        TempFragment fragment = new TempFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temp, container, false);

        tv_main_tempNow = view.findViewById(R.id.tv_main_tempNow);
        getLocation = new GetCurrentLocation(getActivity());
        getTime = new GetCurrentTime();


        WeatherApiInterface apiService = RetrofitInstance.getRetrofitInstance().create(WeatherApiInterface.class);

        weatherViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new WeatherViewModel(apiService);
            }
        }).get(WeatherViewModel.class);

        fetchWeatherWithLocation();

        return view;
    }

    private void fetchWeatherWithLocation() {
        Log.d("WeatherFragment", "fetching data with hardcoded location");

        // 하드코딩된 위도와 경도 (예: 서울)
        double latitude = 37.5665;  // 서울의 위도
        double longitude = 126.9780; // 서울의 경도

        // 위도/경도를 nx, ny로 변환
        ConvertLocation.GridLocation gridLocation = ConvertLocation.convertToGrid(latitude, longitude);

        String currentDate = getTime.getCurrentDate();
        String currentTime = getTime.getCurrentTime();

        // 하드코딩된 격자 좌표로 API 요청
        weatherViewModel.fetchWeatherData(MyServiceKey, 100, 1, "JSON", currentDate, currentTime, 60, 127);

        // Observe and display weather data
        observeWeatherData();
    }

    private void observeWeatherData() {
        Log.d("WeatherFragment", "observeWeatherData: ");
        weatherViewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<WeatherData>() {
            @Override
            public void onChanged(WeatherData weatherData) {
                if (weatherData == null) {
                    Log.e("WeatherFragment", "WeatherData is null");
                    tv_main_tempNow.setText("no data");
                    return;
                }

                String Temp = weatherData.getFcstValue("TMP", getTime.getCurrentDate(), getTime.getCurrentTime());

                if (Temp != null) {
                    tv_main_tempNow.setText(Temp + "°");
                } else {
                    Log.e("WeatherFragment", "getFcstValue returned null");
                    tv_main_tempNow.setText("no data");
                }


            }
        });
    }
}