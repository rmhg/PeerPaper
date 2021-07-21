package com.rzierfiz.peerpaper.UI.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.UI.Peer.PeerActivity;

public class SecondMenu extends Fragment {
    Button manual = null, settings = null;

    public SecondMenu() {
        super(R.layout.second_menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manual = view.findViewById(R.id.manmenu);
        settings = view.findViewById(R.id.setmenu);

        manual.setOnClickListener((v) -> {
            onManual();
        });
        settings.setOnClickListener((v) -> {
            onSetting();
        });
    }

    public void onManual() {
        Intent intent = new Intent(this.getActivity(), PeerActivity.class);
        startActivity(intent);
    }

    public void onSetting() {
    }
}
