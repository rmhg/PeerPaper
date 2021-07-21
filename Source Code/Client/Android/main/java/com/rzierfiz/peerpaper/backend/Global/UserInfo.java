package com.rzierfiz.peerpaper.backend.Global;

import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {

    public String pname = "";
    public String pabout = "";
    public boolean discover = false;
    public LoginCred loginCred = new LoginCred();

    public UserInfo(){
        loginCred.uid = Build.MANUFACTURER + Build.MODEL;
    }

    public JSONObject getObj() throws JSONException {
        JSONObject pobj = new JSONObject();
        pobj.accumulate("uid", loginCred.uid);
        pobj.accumulate("password", loginCred.password);
        pobj.accumulate("discover", discover);
        JSONObject pbobj = new JSONObject();
        pbobj.accumulate("name", pname);
        pbobj.accumulate("about", pabout);
        pobj.accumulate("pbinfo", pbobj);

        return pobj;
    }
}
