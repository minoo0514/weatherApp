package com.example.weatherui.AddFragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.weatherui.R;
import com.example.weatherui.RoomExcel.ExcelData;
import com.example.weatherui.RoomExcel.ExcelDataAdapter;
import com.example.weatherui.RoomExcel.ExcelDataDatabase;
import com.example.weatherui.RoomExcel.ExcelReader;
import com.example.weatherui.RoomSelectRegion.SelectedRegion;
import com.example.weatherui.RoomSelectRegion.SelectedRegionDatabase;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddRegionBottomSheet extends BottomSheetDialogFragment {

    private SearchView searchRegion;
    private ExcelDataAdapter adapter;
    private ExecutorService executorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_region, container, false);

        // ExecutorService 초기화
        executorService = Executors.newSingleThreadExecutor();

        // SearchView와 RecyclerView 초기화
        SearchView searchView = view.findViewById(R.id.search_region);
        RecyclerView recyclerView = view.findViewById(R.id.region_list);

        // 어댑터 초기화 및 RecyclerView에 설정
        adapter = new ExcelDataAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 어댑터에 클릭 리스너 설정
        adapter.setOnItemClickListener(new ExcelDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ExcelData excelData) {
                // 선택된 지역 데이터를 별도의 데이터베이스에 저장
                saveSelectedRegionToDatabase(excelData);
            }
        });

        // 검색어가 입력될 때마다 RecyclerView를 업데이트
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                filterLocations(newText);
                return true;
            }
        });

        checkDatabaseContents();

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

    private void filterLocations(final String query) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // 검색어 전처리: 공백 제거 및 UTF-8 인코딩
                String formattedQuery;
                try {
                    formattedQuery = "%" + new String(query.trim().getBytes("UTF-8"), "UTF-8") + "%";
                } catch (UnsupportedEncodingException e) {
                    formattedQuery = "%" + query.trim() + "%"; // 인코딩에 실패하면 기본 문자열을 사용
                    e.printStackTrace();
                }

                // 로그로 검색어 확인
                Log.d("filterLocations", "Query: " + formattedQuery);

                // 데이터베이스 쿼리 실행
                ExcelDataDatabase db = Room.databaseBuilder(getContext(),
                        ExcelDataDatabase.class, "excel-data-database").build();

                final List<ExcelData> filteredList = db.excelDataDao().searchLocations(formattedQuery);

                Log.d("filterLocations", "Filtered List Size: " + filteredList.size());

                // UI 업데이트
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateData(filteredList);
                        Log.d("RecyclerView", "Adapter item count: " + adapter.getItemCount());
                    }
                });
            }
        });
    }



    private void saveSelectedRegionToDatabase(final ExcelData excelData) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                SelectedRegionDatabase db = Room.databaseBuilder(getContext(),
                        SelectedRegionDatabase.class, "selected-region-database").build();

                SelectedRegion selectedRegion = new SelectedRegion();
                selectedRegion.setRegionName(excelData.getStep1() + " " + excelData.getStep2() + " " + excelData.getStep3());
                selectedRegion.setStep1(excelData.getStep1());
                selectedRegion.setStep2(excelData.getStep2());
                selectedRegion.setStep3(excelData.getStep3());
                selectedRegion.setLongitude(excelData.getLongitude());
                selectedRegion.setLatitude(excelData.getLatitude());

                db.selectedRegionDao().insert(selectedRegion);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "지역이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // 데이터베이스 확인 메서드 추가
    private void checkDatabaseContents() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ExcelDataDatabase db = Room.databaseBuilder(getContext(),
                        ExcelDataDatabase.class, "excel-data-database").build();

                List<ExcelData> allData = db.excelDataDao().getAll(); // 전체 데이터 조회 메서드

                // 전체 데이터 크기 로그
                Log.d("checkDatabaseContents", "Total data count in DB: " + allData.size());

                for (ExcelData data : allData) {
                    Log.d("checkDatabaseContents", "Data: " + data.getStep1() + " " + data.getStep2() + " " + data.getStep3());
                }
            }
        });
    }
}