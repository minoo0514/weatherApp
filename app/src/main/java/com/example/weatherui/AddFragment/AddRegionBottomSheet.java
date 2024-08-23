package com.example.weatherui.AddFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.weatherui.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class AddRegionBottomSheet extends BottomSheetDialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_region, container, false);

       return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // BottomSheetDialog의 높이를 조절하여 키보드와 적절한 간격을 유지하면서 위에 메인화면이 보이도록 합니다.
        View bottomSheet = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);

        int windowHeight = getResources().getDisplayMetrics().heightPixels;
        int desiredHeight = (int) (windowHeight * 0.43); // 화면의 43%
        bottomSheet.getLayoutParams().height = desiredHeight;

        behavior.setPeekHeight(desiredHeight); // 설정한 높이까지만 확장되도록 설정
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // BottomSheet를 초기 상태로 설정
    }
}