package com.example.cargo_app_3.ui.theme.ercancam.cargotracking.shipments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrdersPagerAdapter extends FragmentStateAdapter {

    public OrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new AllShipmentsFragment();
        else
            return new AcceptedShipmentsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
