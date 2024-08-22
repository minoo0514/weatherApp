package com.example.weatherui.AddFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.weatherui.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.widget.SearchView;

public class AddRegionBottomSheet extends BottomSheetDialogFragment {

    private SearchView searchRegion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_region, container, false);

        // 검색창(SearchView)을 찾아옵니다.
        searchRegion = view.findViewById(R.id.search_region);

        // SearchView에 초점을 맞추고 키보드를 자동으로 표시합니다.
        searchRegion.setIconified(false);  // 검색창이 기본적으로 펼쳐지도록 설정
        searchRegion.requestFocus();

        // 키보드 자동 표시 (필요할 경우)
        searchRegion.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(searchRegion.findFocus(), InputMethodManager.SHOW_IMPLICIT);
            }
        }, 30);

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