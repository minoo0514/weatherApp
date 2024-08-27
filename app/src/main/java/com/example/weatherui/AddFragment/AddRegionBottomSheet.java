package com.example.weatherui.AddFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherui.R;
import com.example.weatherui.RegionAdapter;
import com.example.weatherui.RegionData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class AddRegionBottomSheet extends BottomSheetDialogFragment {

    private SearchView search_region;
    private RecyclerView recyclerView;
    private RegionAdapter regionAdapter;
    private List<RegionData> regionList;      // 모든 지역을 저장하는 리스트
    private List<RegionData> filteredList;    // 필터링된 지역을 저장하는 리스트
    private OnRegionSelectedListener listener;

    public AddRegionBottomSheet() {
    }

    public AddRegionBottomSheet(OnRegionSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_region, container, false);

        search_region = view.findViewById(R.id.search_region);
        recyclerView = view.findViewById(R.id.region_list);

        regionList = getRegionList();// 하드코딩된 지역 리스트
        filteredList = new ArrayList<>();
        regionAdapter = new RegionAdapter(regionList, new RegionAdapter.OnRegionClickListener() {
            @Override
            public void onRegionClick(RegionData region) {
                listener.onRegionSelected(region);  // 선택된 지역 정보를 MainActivity에 전달
                dismiss();  // BottomSheet 닫기
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(regionAdapter);

        search_region.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRegionList(newText);
                return true;
            }
        });

        return view;
    }

    private void filterRegionList(String query) {
        filteredList.clear();
        if (!query.isEmpty()) {
            for (RegionData region : regionList) {
                if (region.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(region);
                }
            }
        }
        regionAdapter.updateList(filteredList);
    }

    private List<RegionData> getRegionList() {
        List<RegionData> regionList = new ArrayList<>();
        regionList.add(new RegionData("서울특별시", "1100000000", "60", "127"));
        regionList.add(new RegionData("부산광역시", "2600000000", "98", "76"));
        regionList.add(new RegionData("대구광역시", "2700000000", "89", "90"));
        regionList.add(new RegionData("인천광역시", "2800000000", "55", "124"));
        regionList.add(new RegionData("광주광역시", "2900000000", "58", "74"));
        regionList.add(new RegionData("울산광역시", "3100000000", "102", "84"));
        regionList.add(new RegionData("대전광역시", "3000000000", "67", "100"));

        return  regionList;
    }

    public interface OnRegionSelectedListener {
        void onRegionSelected(RegionData regionData);
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

        search_region.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(search_region, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}