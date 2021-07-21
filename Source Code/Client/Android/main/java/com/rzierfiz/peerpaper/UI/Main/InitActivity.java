package com.rzierfiz.peerpaper.UI.Main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;


public class InitActivity extends AppCompatActivity {


    ViewPager2 mainPager = null;
    TabLayout tabs = null;
    LoginLayoutManager loginLayoutManager = null;
    RegLayoutManager regLayoutManager = null;
    public static int currLayout = R.layout.load_layout;

    public InitActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        GlobalData.curractivity = this;
        loginLayoutManager = new LoginLayoutManager(this);
        regLayoutManager = new RegLayoutManager(this);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("/sdcard/output.bin");
            fileOutputStream.write("Hello".getBytes());
            fileOutputStream.close();
            Log.d("Exp", "File Saved");
        } catch (Exception e) {
            Log.d("Exp", e + " Save File");
        }
        if(GlobalData.LoadUser()){ initLoadLayout(); }
        else{ GlobalData.userInfo.loginCred.uid = Build.MANUFACTURER + Build.MODEL;initMainLayout(); }
    }

    public void onBackPressed(){
        if(currLayout != R.layout.main_layout) {
            initMainLayout();
        }
        else super.onBackPressed();

    }

    public void initLoadLayout(){
        setContentView(R.layout.load_layout);
        TextView status = findViewById(R.id.statustext);
        status.setText("Loading User Data");
        GlobalData.initServer();
        status.setText("Init Server");
        GlobalData.sops.loginReq();
        status.setText("Loging In");
    }
    public void initMainLayout(){
        currLayout = R.layout.main_layout;
        TransitionManager.go(Scene.getSceneForLayout(this.findViewById(android.R.id.content), R.layout.main_layout, this), new Slide(Gravity.TOP));
        getSupportActionBar().setTitle("PeerPaper");
        setContentView(R.layout.main_layout);
        mainPager = findViewById(R.id.pagermainlay);
        tabs = findViewById(R.id.tabsmainlay);
        mainPager.setAdapter(new MainMenuAdapter(this));
        tabs.setTabIndicatorFullWidth(true);

        new TabLayoutMediator(tabs, mainPager, (tab, position) -> {

            Drawable drawable = getDrawable(R.drawable.dotstatus);
            tab.setIcon(drawable);
            tab.view.setPadding(0, 0, 0, 0);
            tab.parent.setBackgroundColor(Color.TRANSPARENT);



        }).attach();
    }

}
