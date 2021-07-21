package com.rzierfiz.peerpaper.UI.Main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rzierfiz.peerpaper.backend.Global.GlobalData;

public class MainMenuAdapter extends FragmentStateAdapter {

    final int cPages = 2;
    Fragment[] fLayouts = {new FirstMenu(), new SecondMenu()};

    public MainMenuAdapter(FragmentActivity activity) {
        super(activity);
    }

    public Fragment createFragment(int position) {
        return fLayouts[position];

    }

    @Override
    public int getItemCount() {
        return cPages;
    }
}
