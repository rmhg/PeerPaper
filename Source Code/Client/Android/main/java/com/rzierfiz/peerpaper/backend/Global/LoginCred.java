package com.rzierfiz.peerpaper.backend.Global;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginCred {
    public int timestamp = 0;
    public boolean isvalid = false;
    public String uid = "";
    public String password = "";
    public TokenCred tokenCred = new TokenCred();

    public JSONObject getObj() throws JSONException {
        JSONObject pobj = new JSONObject();
        pobj.accumulate("uid", uid);
        pobj.accumulate("password", password);
        return pobj;
    }
}
