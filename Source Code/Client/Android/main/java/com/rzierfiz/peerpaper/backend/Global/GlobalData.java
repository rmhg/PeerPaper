package com.rzierfiz.peerpaper.backend.Global;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rzierfiz.peerpaper.UI.Main.InitActivity;
import com.rzierfiz.peerpaper.UI.Peer.PeerActivity;
import com.rzierfiz.peerpaper.backend.PeerConnection.Discovery.Discover;
import com.rzierfiz.peerpaper.backend.PeerConnection.Discovery.DiscoverPeerInfo;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConnection;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConversation;
import com.rzierfiz.peerpaper.backend.ServerConnection.Responder;
import com.rzierfiz.peerpaper.backend.ServerConnection.ServerConnection;
import com.rzierfiz.peerpaper.backend.ServerConnection.ServerOperations;

import java.util.HashMap;
import java.util.Map;

public class GlobalData {
    public static ServerConnection serverConnectionInstance = null;
    public static ServerOperations sops = null;
    public static UserInfo userInfo = new UserInfo();
    public static String sip = "192.168.43.205";
    public static int sport = 5000;
    public static Map<String, PeerConnection> peers = new HashMap<>();
    public static AppCompatActivity curractivity = null;
    public static Responder res = new Responder();
    public static FindPeer findPeer = new FindPeer();
    public static UserInfo tmpuserInfo = new UserInfo();
    public static final int disBindPort = 5000;
    public static Discover discover = new Discover();
    public static Map<String, DiscoverPeerInfo> dpeers = new HashMap<>();
    public enum CURRENT{
        START,
        INIT,
        LOGGED
    };
    public static CURRENT current = CURRENT.START;
    public static int BindPort = 5000;

    public static void initServer(){
        try{
            if(serverConnectionInstance instanceof ServerConnection){
                serverConnectionInstance.Connect();
            }else{
                serverConnectionInstance = new ServerConnection(sip, sport);
                serverConnectionInstance.BindResponder(res);
                sops = new ServerOperations(userInfo, serverConnectionInstance);
            }
            current = CURRENT.INIT;
        }catch (Exception e){
            Log.d("Exp", "Exception " + e);
            e.printStackTrace();
        }

    }


    public static void showText(String text){
        curractivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(curractivity, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showTextShort(String text){
        curractivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(curractivity, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void LoginUI(){
        current = CURRENT.LOGGED;
        curractivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(curractivity, PeerActivity.class);
                curractivity.startActivity(intent);
                curractivity.finish();
            }
        });
    }

    public static void onConnectionFailed(){
        if(GlobalData.curractivity instanceof InitActivity){
            GlobalData.showTextShort("Can't Connect To Server");
             GlobalData.curractivity.runOnUiThread(((InitActivity)GlobalData.curractivity)::initMainLayout);
        }
    }

    public static boolean LoadUser(){
        //Load UID and Password
        SharedPreferences sharedPreferences = curractivity.getPreferences(Context.MODE_PRIVATE);
        userInfo.loginCred.uid = sharedPreferences.getString("uid", null);
        userInfo.loginCred.password = sharedPreferences.getString("password", null);
        return (userInfo.loginCred.uid != null || userInfo.loginCred.password != null);
    }

    public static void ClearUser(){
        SharedPreferences sharedPreferences = curractivity.getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public static void SaveUser(){
        //Save UID, Password
        SharedPreferences sharedPreferences = curractivity.getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("uid", userInfo.loginCred.uid).putString("password", userInfo.loginCred.password).apply();
    }

}
