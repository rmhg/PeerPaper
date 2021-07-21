package com.rzierfiz.peerpaper.UI.Main;

import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;

public class LoginLayoutManager {
    EditText uidet = null, passwordet = null;
    Button forgotbut = null, continuebut = null;
    final int LAYOUT = R.layout.login_layout;
    AppCompatActivity curr = null;

    public LoginLayoutManager(AppCompatActivity activity){
        curr = activity;
    }

    public int set(){
        View root = curr.findViewById(android.R.id.content);
        TransitionManager.go(Scene.getSceneForLayout((ViewGroup) root, LAYOUT, curr), new Slide(Gravity.BOTTOM));
        curr.getSupportActionBar().setTitle("Login");
        curr.setContentView(LAYOUT);
        uidet = curr.findViewById(R.id.uidlogin);
        passwordet = curr.findViewById(R.id.passlogin);
        forgotbut = curr.findViewById(R.id.forgotlogin);
        continuebut = curr.findViewById(R.id.continuelogin);
        forgotbut.setOnClickListener(this::forgot);
        continuebut.setOnClickListener(this::cont);
        return LAYOUT;
    }

    private void forgot(View v){
        String uid = uidet.getText().toString();
        String password = passwordet.getText().toString();

        onForgot(uid, password);

    }
    private void cont(View v){
        String uid = uidet.getText().toString();
        String password = passwordet.getText().toString();

        onContinue(uid, password);
    }

    public void onForgot(String uid, String password){}
    public void onContinue(String uid, String password){
        GlobalData.initServer();
        GlobalData.userInfo.loginCred.password = password;
        GlobalData.userInfo.loginCred.uid = uid;
        GlobalData.sops.loginReq();
    }

}
