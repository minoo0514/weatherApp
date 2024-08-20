package com.example.weatherui.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TempFragment();
            case 1:
                return new RainFragment();
            case 2:
                return new ScheduleFragment();
            default:
                return new TempFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
