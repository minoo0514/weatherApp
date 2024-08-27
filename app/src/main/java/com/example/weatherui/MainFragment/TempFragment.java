package com.example.weatherui.MainFragment;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherui.Location.ConvertLocation;
import com.example.weatherui.Location.GetCurrentLocation;
import com.example.weatherui.Location.GetCurrentTime;
import com.example.weatherui.R;
import com.example.weatherui.ScheduleActivity;
import com.example.weatherui.api.RetrofitInstance;
import com.example.weatherui.api.WeatherApiInterface;
import com.example.weatherui.api.WeatherData;
import com.example.weatherui.api.WeatherViewModel;
import com.example.weatherui.api.WindChillData;
import com.example.weatherui.kakaoapi.KakaoViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TempFragment extends Fragment {

    private GetCurrentLocation  getLocation;
    private GetCurrentTime getTime;

    private TextView tv_main_tempNow;
    private TextView tv_main_weatherCondition;
    private TextView tv_main_tempMax;
    private TextView tv_main_tempMin;
    private TextView tv_main_windChillTemp;
    private ImageView iv_tempFragment_ic;
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
        tv_main_weatherCondition = view.findViewById(R.id.tv_main_weatherCondition);
        tv_main_tempMax = view.findViewById(R.id.tv_main_tempMax);
        tv_main_tempMin = view.findViewById(R.id.tv_main_tempMin);
        tv_main_windChillTemp = view.findViewById(R.id.tv_main_windChillTemp);
        iv_tempFragment_ic = view.findViewById(R.id.iv_tempFragment_ic);

        Button goToScheduleButton = view.findViewById(R.id.btn_tempFragment_goSch);
        goToScheduleButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScheduleActivity.class);
            startActivity(intent);
        });

        getLocation = new GetCurrentLocation(getActivity());
        getTime = new GetCurrentTime();

        WeatherApiInterface apiService = RetrofitInstance.getWeatherRetrofitInstance().create(WeatherApiInterface.class);

        weatherViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new WeatherViewModel(apiService);
            }
        }).get(WeatherViewModel.class);

        requestLocationPermissionAndFetchWeather();

        return view;
    }

    private void requestLocationPermissionAndFetchWeather() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한이 허용되었을 때 현재 위치를 가져옴
                getLocation.fetchLocation(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            Log.d("onPermissionGranted", "latitude: "+ latitude + "longitude: " + longitude);
                            // 현재 위치의 위도/경도를 격자 좌표로 변환
                            ConvertLocation.GridLocation gridLocation = ConvertLocation.convertToGrid(latitude, longitude);
                            String nx = String.valueOf(gridLocation.nx);
                            String ny = String.valueOf(gridLocation.ny);
                            Log.d("onPermissionGranted", "nx:" + nx + "ny:" + ny);
                            // 격자 좌표를 사용하여 API 요청
                            fetchWeatherWithLocation(nx, ny);
                        } else {
                            Toast.makeText(getActivity(), "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getActivity(), "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleMessage("현재 위치를 사용하려면 위치 권한이 필요합니다.")
                .setDeniedMessage("위치 권한이 거부되었습니다. 설정에서 권한을 허용해 주세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }
    private void fetchWeatherWithLocation(String nx, String ny) {
        String currentDate = getTime.getCurrentDate();
        String currentTime = getTime.getCurrentTime();
        Log.d("fetchWeatherWithLocation", "currentTime: " +currentTime);

        // 현재 위치 기반으로 API 요청
        weatherViewModel.fetchWeatherData(MyServiceKey, 100, 1, "JSON", currentDate, currentTime, nx, ny);
        observeWeatherData();

        weatherViewModel.fetchWeatherData(MyServiceKey, 500, 1, "JSON", currentDate, "0200", nx, ny);
        observeTempMaxMinData();

        String currentDateTime = getCurrentDateTime();
        weatherViewModel.fetchWindChillData(MyServiceKey, 100, 1, "JSON", "1100000000", currentDateTime, "A41");
        observeWindChillData();
    }

    private void observeWeatherData() {
        Log.d("WeatherFragment", "observeWeatherData: ");
        weatherViewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<WeatherData>() {
            @Override
            public void onChanged(WeatherData weatherData) {
                if (weatherData == null) {
                    Log.e("WeatherFragment", "WeatherData is null");
                    tv_main_tempNow.setText("no data");
                    tv_main_weatherCondition.setText("no data");
                    return;
                }

                String Sky = weatherData.getFcstValue("SKY", getTime.getCurrentDate(), "0900");
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

                String currentTime = getTime.getCurrentTime();
                String TemporalTime = getNextHour(currentTime);

                String PTY = weatherData.getFcstValue("PTY", getTime.getCurrentDate(), TemporalTime);

                if (Objects.equals(PTY, "0")) {
                    if (Objects.equals(Sky, "1")) {
                        iv_tempFragment_ic.setImageResource(R.drawable.a_sun_180dp);
                    } else if (Objects.equals(Sky, "3")) {
                        iv_tempFragment_ic.setImageResource(R.drawable.a_sun_cloud_180dp);
                    } else if (Objects.equals(Sky, "4")) {
                        iv_tempFragment_ic.setImageResource(R.drawable.a_cloud_180dp);
                    }
                } else if (Objects.equals(PTY, "1")) {
                    iv_tempFragment_ic.setImageResource(R.drawable.a_rain_180dp);
                } else if (Objects.equals(PTY, "2")) {
                    iv_tempFragment_ic.setImageResource(R.drawable.a_rain_180dp);
                } else if (Objects.equals(PTY, "3")) {
                    iv_tempFragment_ic.setImageResource(R.drawable.a_snow_180dp);
                } else if (Objects.equals(PTY, "4")) {
                    iv_tempFragment_ic.setImageResource(R.drawable.a_rain_180dp);
                }

                String Temp = weatherData.getFcstValue("TMP", getTime.getCurrentDate() , TemporalTime);
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

    private String getNextHour(String currentTime) {
        // currentTime을 정수로 변환하여 100을 더합니다.
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
}