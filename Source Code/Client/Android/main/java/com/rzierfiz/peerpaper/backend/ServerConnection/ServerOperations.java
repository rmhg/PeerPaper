package com.rzierfiz.peerpaper.backend.ServerConnection;


import android.util.Log;
import android.widget.Toast;

import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.Global.UserInfo;

import org.json.JSONObject;

/*
* login
* renew
* register
* findpeer
* connection
* configure(logout, change)
* */

public class ServerOperations {
        UserInfo userInfo = null;
        ServerConnection serverConnection = null;
        
        public ServerOperations(UserInfo cui, ServerConnection conn){
                userInfo = cui; serverConnection = conn;
        }

        public void registerReq(UserInfo userinfo) {
                PreDo();
                try {
                        JSONObject obj = new JSONObject();
                        obj.accumulate("type", "reg");
                        obj.accumulate("status", 0);
                        JSONObject mobj = new JSONObject();
                        mobj.accumulate("objname", "regobj");
                        mobj.accumulate("regobj", userinfo.getObj());
                        obj.accumulate("obj", mobj);
                        serverConnection.sendObj(obj);
                }catch (Exception e){

                }

        }
        public void loginReq() {
                PreDo();
                try {
                        JSONObject obj = new JSONObject();
                        obj.accumulate("type", "auth");
                        obj.accumulate("status", 0);
                        JSONObject mobj = new JSONObject();
                        mobj.accumulate("objname", "authobj");
                        mobj.accumulate("authobj", userInfo.loginCred.getObj().accumulate("type", "new"));
                        obj.accumulate("obj", mobj);
                        new Thread(new Runnable() {
                                @Override
                                public void run() {
                                        try {
                                                serverConnection.sendObj(obj);
                                        }catch(Exception e){}
                                }
                        }).start();
                }catch (Exception e){
                        Toast.makeText(GlobalData.curractivity, "Exception In Thus" + e, Toast.LENGTH_LONG).show();
                }
        }
        public void renewReq() {
                PreDo();
                try {
                        JSONObject obj = new JSONObject();
                        obj.accumulate("type", "auth");
                        obj.accumulate("status", 0);
                        JSONObject mobj = new JSONObject();
                        mobj.accumulate("objname", "authobj");
                        mobj.accumulate("authobj", userInfo.loginCred.getObj().accumulate("type", "token"));
                        obj.accumulate("obj", mobj);
                        serverConnection.sendObj(obj);
                }catch (Exception e){

                }
        }

        public void findpeerReq(String puid) {
                PreDo();
                try{
                        JSONObject obj = new JSONObject();
                        obj.accumulate("type", "findpeer");
                        obj.accumulate("status", 0);
                        JSONObject mobj = new JSONObject();
                        mobj.accumulate("objname", "findpeerobj");
                        mobj.accumulate("findpeerobj", new JSONObject().accumulate("puid", puid).accumulate("tokenobj", userInfo.loginCred.tokenCred.getObj()));
                        obj.accumulate("obj", mobj);
                        serverConnection.sendObj(obj);
                }
                catch (Exception e){}
        }
        public void connectReq(String puid, String pmsg) {
                PreDo();
                try{
                        JSONObject obj = new JSONObject();
                        obj.accumulate("type", "connect");
                        obj.accumulate("status", 0);
                        JSONObject mobj = new JSONObject();
                        mobj.accumulate("objname", "connectobj");
                        mobj.accumulate("connectobj", new JSONObject().accumulate("puid", puid).accumulate("pmsg", pmsg).accumulate("tokenobj", userInfo.loginCred.tokenCred.getObj()));
                        obj.accumulate("obj", mobj);
                        serverConnection.sendObj(obj);
                }
                catch (Exception e){}
        }

        public void logoutReq(){
                PreDo();
                try{
                        JSONObject obj = new JSONObject();
                        obj.accumulate("type", "config");
                        obj.accumulate("status", 0);
                        JSONObject mobj = new JSONObject();
                        mobj.accumulate("objname", "configobj");
                        mobj.accumulate("confobj", new JSONObject().accumulate("type", "logout").accumulate("tokenobj", userInfo.loginCred.tokenCred.getObj()));
                        obj.accumulate("obj", mobj);
                        serverConnection.sendObj(obj);
                }
                catch (Exception e){}
        }

        public void changeReq(){
                PreDo();
                try{
                        UserInfo userinfo = GlobalData.tmpuserInfo;
                        JSONObject obj = new JSONObject();
                        obj.accumulate("type", "config");
                        obj.accumulate("status", 0);
                        JSONObject mobj = new JSONObject();
                        mobj.accumulate("objname", "configobj");
                        JSONObject confobj = new JSONObject().accumulate("type", "chg").accumulate("tokenobj", userInfo.loginCred.tokenCred.getObj());
                        if(userinfo.discover != GlobalData.userInfo.discover) confobj.accumulate("discover", userinfo.discover);
                        if(userinfo.loginCred.password.length() > 0) mobj.accumulate("passwrd", userinfo.loginCred.password);
                        JSONObject pbobj = new JSONObject();
                        if(userinfo.pname.length() > 0) {
                                pbobj.accumulate("name", userinfo.pname);
                                mobj.accumulate("pbinfo", pbobj);
                        }
                        if(userinfo.pabout.length() > 0){
                                pbobj.accumulate("about", userinfo.pabout);
                                mobj.accumulate("pbinfo", pbobj);
                        }

                        mobj.accumulate("confobj", confobj);
                        obj.accumulate("obj", mobj);
                        serverConnection.sendObj(obj);
                }
                catch (Exception e){}
        }


        private static void PreDo(){
                if(GlobalData.current == GlobalData.CURRENT.START) GlobalData.initServer();
        }

}
