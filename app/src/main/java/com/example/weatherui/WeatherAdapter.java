package com.example.weatherui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<RegionWeatherData> regionWeatherDataList;

    public WeatherAdapter(List<RegionWeatherData> regionWeatherDataList) {
        this.regionWeatherDataList = regionWeatherDataList;
    }
    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        RegionWeatherData regionWeatherData = regionWeatherDataList.get(position);
        holder.bind(regionWeatherData);
    }

    @Override
    public int getItemCount() {
        return regionWeatherDataList.size();
    }
    public void updateWeatherItems(List<RegionWeatherData> newRegionWeatherData) {
        this.regionWeatherDataList = newRegionWeatherData;
        notifyDataSetChanged();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView regionNameTextView;
        private TextView TempTextView;


        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            regionNameTextView = itemView.findViewById(R.id.regionNameTextView);
            TempTextView = itemView.findViewById(R.id.TempTextView);

        }

        public void bind(RegionWeatherData regionWeatherData) {
            regionNameTextView.setText(regionWeatherData.getRegionName());

            // TMP 카테고리의 값만 설정
            if ("TMP".equals(regionWeatherData.getWeatherItem().category)) {
                TempTextView.setText(regionWeatherData.getWeatherItem().fcstValue);
                TempTextView.setVisibility(View.VISIBLE);
            } else {
                TempTextView.setVisibility(View.GONE);
            }
        }
    }
}


