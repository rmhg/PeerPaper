package com.rzierfiz.peerpaper.backend.Global;

import com.rzierfiz.peerpaper.UI.Peer.PeerCard;

import org.json.JSONObject;



public class PeerInfo {
    public String puid = "";
    public String ip = "";
    public int port = 0;
    public boolean isvalid = false;
    public boolean isconnectable = false;
    public boolean isconnected = false;
    public long timestamp = 0;
    public String usecret = "", psecret = "";
    public enum STATUS{
        CONNECTED,
        DISCONNECTED,
        REQUESTED,
        PENDING
    }
    public PeerInfo(){}
    public PeerInfo(JSONObject obj){
        try{
            puid = obj.getString("puid");
            isvalid = true;
            timestamp = System.currentTimeMillis();
            String sock = obj.getString("psockaddr");
            if(obj != null){
                String[] ipp = sock.split(":");
                ip = ipp[0];port = Integer.parseInt(ipp[1]);
                isconnectable = true;
                usecret = obj.getString("usecret");
                psecret = obj.getString("psecret");
            }


        }catch (Exception e){

        }
    }
    public STATUS getStatus(){
        if(isconnectable){
            if ((isconnected)) {
                return STATUS.CONNECTED;
            } else {
                return STATUS.DISCONNECTED;
            }
        }
        else{
            if(isvalid)
                return STATUS.REQUESTED;
            else
                return STATUS.PENDING;
        }
    }

}
