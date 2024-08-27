package com.example.weatherui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherui.R;

import java.util.List;

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

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.dateTextView.setText(event.getDate()); // yyyyMMdd HHmm 형식으로 저장된 날짜
        holder.titleTextView.setText(event.getTitle());

        holder.checkBox.setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);
        holder.checkBox.setChecked(event.isSelected());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            event.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView titleTextView;
        CheckBox checkBox;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            checkBox = itemView.findViewById(R.id.event_checkBox); // 체크박스 추가
        }
    }
}

