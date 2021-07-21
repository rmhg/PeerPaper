package com.rzierfiz.peerpaper.backend.Global;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenCred {

    public int timestamp = 0;
    public String uid = "";
    public String tid = "", tstr = "";
    public boolean isvalid = false;

    public JSONObject getObj() throws JSONException{
        JSONObject obj = new JSONObject();
        obj.accumulate("tid", tid);
        obj.accumulate("tstr", tstr);
        obj.accumulate("uid", uid);
        return obj;
    }


}
