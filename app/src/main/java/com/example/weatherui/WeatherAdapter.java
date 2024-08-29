package com.example.weatherui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherui.api.WeatherData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<RegionWeatherData> regionWeatherDataList;
    private boolean isDeleteMode = false; // 삭제 모드 여부를 추적하는 필드


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
        holder.bind(regionWeatherData, isDeleteMode);
    }

    @Override
    public int getItemCount() {
        return regionWeatherDataList.size();
    }

    public List<RegionWeatherData> getRegionWeatherDataList() {
        return regionWeatherDataList;
    }

    public void updateWeatherItems(List<RegionWeatherData> newRegionWeatherData) {
        Set<RegionWeatherData> uniqueDataSet = new HashSet<>(regionWeatherDataList);
        uniqueDataSet.addAll(newRegionWeatherData);

        regionWeatherDataList.clear();
        regionWeatherDataList.addAll(uniqueDataSet);

        notifyDataSetChanged();
    }

    // 삭제 모드 설정 메서드
    public void setDeleteMode(boolean isDeleteMode) {
        this.isDeleteMode = isDeleteMode;
        notifyDataSetChanged();
    }

    // 선택된 항목 삭제 메서드
    public void deleteSelectedItems() {
        List<RegionWeatherData> selectedItems = new ArrayList<>();
        for (RegionWeatherData item : regionWeatherDataList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        regionWeatherDataList.removeAll(selectedItems);
        notifyDataSetChanged();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {

        private final TextView regionNameTextView;
        private final TextView TempTextView;
        private final CheckBox deleteCheckBox; // 삭제 모드에서 사용되는 체크박스

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            regionNameTextView = itemView.findViewById(R.id.regionNameTextViewCard);
            TempTextView = itemView.findViewById(R.id.TempTextView);
            deleteCheckBox = itemView.findViewById(R.id.Region_checkBox);
        }

        public void bind(RegionWeatherData regionWeatherData, boolean isDeleteMode) {
            regionNameTextView.setText(regionWeatherData.getRegionName());

            WeatherData.Item weatherItem = regionWeatherData.getWeatherItem();
            if (weatherItem != null && "TMP".equals(weatherItem.category)) {
                TempTextView.setText(weatherItem.fcstValue + "°");
                TempTextView.setVisibility(View.VISIBLE);
                Log.d("WeatherAdapter", "WeatherAdapter: "+ weatherItem.fcstValue);
            } else {
                TempTextView.setVisibility(View.GONE);
                Log.d("WeatherAdapter", "WeatherAdapter: null");
            }

            // 삭제 모드에 따라 체크박스의 가시성을 설정
            deleteCheckBox.setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);
            deleteCheckBox.setChecked(regionWeatherData.isSelected());

            deleteCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> regionWeatherData.setSelected(isChecked));
        }
    }
}



