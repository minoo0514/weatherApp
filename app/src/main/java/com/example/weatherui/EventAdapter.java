package com.example.weatherui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherui.Location.ConvertLocation;
import com.example.weatherui.WeatherApi.WeatherBitData;
import com.example.weatherui.WeatherApi.WeatherBitViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private boolean isDeleteMode = false;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    public void setDeleteMode(boolean deleteMode) {
        isDeleteMode = deleteMode;
        notifyDataSetChanged();
    }

    public void updateEvents(List<Event> events) {
        this.eventList = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        // 로그 출력
        Log.d("EventAdapter", "Event data: " + event.toString());

        // 지역명, 날짜, 제목 설정
        holder.RegionTextView.setText(event.getRegion().getName());
        holder.dateTextView.setText(DateUtils.formatDate(event.getDate()));
        holder.titleTextView.setText(event.getTitle());

        // 날씨 정보 바인딩
        holder.temperatureView.setText(String.format(Locale.getDefault(), "%.1f°C", event.getTemperature()));
        holder.precipitationTextView.setText(String.format(Locale.getDefault(), "%.1fmm", event.getPrecipitationAmount()));
        holder.rainProbabilityTextView.setText(String.format(Locale.getDefault(), "%d%%", event.getPrecipitationProbability()));

        // 구름량에 따른 아이콘 설정
        int cloudCoverage = event.getCloudCoverage();
        if (cloudCoverage <= 20) {
            holder.weatherIconImageView.setImageResource(R.drawable.a_sun_25dp);
        } else if (cloudCoverage <= 50) {
            holder.weatherIconImageView.setImageResource(R.drawable.a_sun_cloud_25dp);
        } else if (cloudCoverage <= 80) {
            holder.weatherIconImageView.setImageResource(R.drawable.a_cloud_25dp);
        } else {
            holder.weatherIconImageView.setImageResource(R.drawable.a_sun_25dp);
        }

        // 삭제 모드 설정
        holder.checkBox.setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);
        holder.checkBox.setChecked(event.isSelected());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> event.setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView weatherIconImageView;
        TextView rainProbabilityTextView;
        TextView dateTextView;
        TextView titleTextView;
        TextView temperatureView;
        CheckBox checkBox;
        TextView precipitationTextView;
        TextView RegionTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            RegionTextView = itemView.findViewById(R.id.region_text_view);
            temperatureView = itemView.findViewById(R.id.temperature_text_view);
            weatherIconImageView = itemView.findViewById(R.id.weather_icon_image_view);
            rainProbabilityTextView = itemView.findViewById(R.id.rain_probability_text_view);
            precipitationTextView = itemView.findViewById(R.id.precipitation_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            checkBox = itemView.findViewById(R.id.event_checkBox);
        }
    }
}


