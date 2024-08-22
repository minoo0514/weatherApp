package com.example.weatherui.AddFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weatherui.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddEventBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_add_event.xml 레이아웃을 인플레이트합니다.
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        // 여기서 필요한 뷰들을 설정합니다.
        // 예: EditText, Button 등의 초기화 작업

        return view;
    }
}
