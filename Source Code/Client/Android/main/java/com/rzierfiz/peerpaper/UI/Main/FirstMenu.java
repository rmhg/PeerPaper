package com.rzierfiz.peerpaper.UI.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;

public class FirstMenu extends Fragment {

    Button loginbut = null, regbut = null;

    public FirstMenu() {
        super(R.layout.first_menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginbut = view.findViewById(R.id.loginmenu);
        regbut = view.findViewById(R.id.regmenu);

        if(GlobalData.serverConnectionInstance.ConnectionFailed){
            DisableButtons();
        }else{
            EnableButtons();
        }


        loginbut.setOnClickListener((v) -> { onLogin(); });
        regbut.setOnClickListener((v) -> { onRegister(); });

    }

    void DisableButtons(){
       loginbut.setEnabled(false);regbut.setEnabled(false);
    }

    void EnableButtons(){
        loginbut.setEnabled(true);regbut.setEnabled(true);
    }

    public void onLogin() {
        InitActivity.currLayout = ((InitActivity)this.getActivity()).loginLayoutManager.set();
    }

    public void onRegister() {
        InitActivity.currLayout = ((InitActivity)this.getActivity()).regLayoutManager.set();
    }
}
