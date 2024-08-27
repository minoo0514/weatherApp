package com.example.weatherui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.RegionViewHolder> {

    private List<RegionData> regionList;
    private OnRegionClickListener onRegionClickListener;

    public interface OnRegionClickListener {
        void onRegionClick(RegionData region);
    }

    public RegionAdapter(List<RegionData> regionList, OnRegionClickListener listener) {
        this.regionList = regionList;
        this.onRegionClickListener = listener;
    }

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_region, parent, false);
        return new RegionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        RegionData region = regionList.get(position);
        holder.bind(region);
    }

    @Override
    public int getItemCount() {
        return regionList.size();
    }

    public void updateList(List<RegionData> filteredList) {
        regionList = filteredList;
        notifyDataSetChanged();
    }

    class RegionViewHolder extends RecyclerView.ViewHolder {
        private TextView regionNameTextView;

        public RegionViewHolder(@NonNull View itemView) {
            super(itemView);
            regionNameTextView = itemView.findViewById(R.id.regionNameTextView);
        }

        public void bind(final RegionData region) {
            regionNameTextView.setText(region.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRegionClickListener.onRegionClick(region);
                }
            });
        }
    }
}


