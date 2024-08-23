package com.example.weatherui.RoomExcel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherui.R;

import java.util.List;

public class ExcelDataAdapter extends RecyclerView.Adapter<ExcelDataAdapter.ExcelDataViewHolder> {

    private List<ExcelData> excelDataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ExcelData excelData);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ExcelDataAdapter(List<ExcelData> excelDataList) {
        this.excelDataList = excelDataList;
    }

    @NonNull
    @Override
    public ExcelDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_excel_data, parent, false);
        return new ExcelDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcelDataViewHolder holder, int position) {
        ExcelData excelData = excelDataList.get(position);
        holder.bind(excelData);
    }

    @Override
    public int getItemCount() {
        return excelDataList.size();
    }

    public void updateData(List<ExcelData> newExcelDataList) {
        excelDataList.clear();
        excelDataList.addAll(newExcelDataList);
        notifyDataSetChanged(); // 데이터가 변경되었음을 RecyclerView에 알림
    }

    public class ExcelDataViewHolder extends RecyclerView.ViewHolder {
        TextView textStep1, textStep2, textStep3;

        public ExcelDataViewHolder(@NonNull View itemView) {
            super(itemView);
            textStep1 = itemView.findViewById(R.id.text_step1);
            textStep2 = itemView.findViewById(R.id.text_step2);
            textStep3 = itemView.findViewById(R.id.text_step3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(excelDataList.get(position));
                    }
                }
            });
        }

        public void bind(ExcelData excelData) {
            textStep1.setText(excelData.getStep1());
            textStep2.setText(excelData.getStep2());
            textStep3.setText(excelData.getStep3());
        }
    }
}

