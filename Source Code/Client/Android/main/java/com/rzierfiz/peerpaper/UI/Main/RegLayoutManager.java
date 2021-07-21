package com.rzierfiz.peerpaper.UI.Main;

import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.Global.UserInfo;

public class RegLayoutManager {
    EditText uidet = null, passwordet = null, confirmpasswordet = null;
    Switch discoverysw = null;
    EditText pnameet = null, paboutet = null;
    Button clearbut = null, continuebut = null;
    final int LAYOUT = R.layout.reg_layout;
    AppCompatActivity curr = null;

    public RegLayoutManager(AppCompatActivity activity){
        curr = activity;

    }

    public int set(){
        curr.getSupportActionBar().setTitle("Registration");
        View root = curr.findViewById(android.R.id.content);
        TransitionManager.go(Scene.getSceneForLayout((ViewGroup) root, LAYOUT, curr), new Slide(Gravity.BOTTOM));
        curr.setContentView(LAYOUT);
        uidet = curr.findViewById(R.id.uidreg);
        passwordet = curr.findViewById(R.id.passreg);
        confirmpasswordet = curr.findViewById(R.id.confirmpassreg);
        discoverysw = curr.findViewById(R.id.discoverreg);
        pnameet = curr.findViewById(R.id.pnamereg);
        paboutet = curr.findViewById(R.id.paboutreg);
        clearbut = curr.findViewById(R.id.clearreg);
        continuebut = curr.findViewById(R.id.continuereg);
        clearbut.setOnClickListener(this::clear);
        continuebut.setOnClickListener(this::cont);
        return LAYOUT;
    }


    private void clear(View v){
        uidet.setText("");
        passwordet.setText("");
        confirmpasswordet.setText("");
        discoverysw.setChecked(false);
        pnameet.setText("");
        paboutet.setText("");


    }
    private void cont(View v){
        String uid = uidet.getText().toString();
        String password = passwordet.getText().toString();
        String confpassword = confirmpasswordet.getText().toString();
        boolean discover = discoverysw.isChecked();
        String pname = pnameet.getText().toString();
        String pabout = paboutet.getText().toString();

        onContinue(uid, password, discover, pname, pabout);
    }

    public void onContinue(String uid, String password, boolean discover, String pname, String pabout){
        UserInfo userInfo = new UserInfo();
        userInfo.discover = discover;
        userInfo.pabout = pabout;
        userInfo.pname = pname;
        userInfo.loginCred.uid = uid;
        userInfo.loginCred.password = password;

        GlobalData.initServer();
        GlobalData.sops.registerReq(userInfo);

    }

}
