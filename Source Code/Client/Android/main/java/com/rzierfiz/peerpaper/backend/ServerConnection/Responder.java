package com.rzierfiz.peerpaper.backend.ServerConnection;

import com.rzierfiz.peerpaper.UI.Peer.AddPeer.MainAddPeerFragment;
import com.rzierfiz.peerpaper.UI.Peer.PeerActivity;
import com.rzierfiz.peerpaper.backend.Global.FindPeer;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.Global.PeerInfo;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.TextMessage;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConnection;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConversation;

import org.json.JSONException;
import org.json.JSONObject;

public class Responder {

    public final void Mapper(JSONObject obj){
        try {
            String type = obj.getString("type");

            JSONObject mobj = obj.getJSONObject("obj");
            JSONObject pobj = mobj.getJSONObject(mobj.getString("objname"));
            switch (type){
                case "status" : onStatus(pobj); break;
                case "token" : onToken(pobj); break;
                case "peerinfo" : onPeerInfo(pobj); break;
                case "connection" : onConnection(pobj); break;
                case "reqpeer" : onReqPeer(pobj); break;
                default: GlobalData.showText("Default : " + obj.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onStatus(JSONObject obj){
        try {
            int code = obj.getInt("code");
            String msg = obj.getString("msg");
            GlobalData.showText("status " + code + " " + msg);
            switch(code){
                case 201 :
                    GlobalData.userInfo.loginCred.isvalid = true;
                    GlobalData.SaveUser();
                    break;
                case 202 :
                    GlobalData.current = GlobalData.CURRENT.START;
                    GlobalData.userInfo.loginCred.tokenCred.isvalid = false;
                    GlobalData.ClearUser();
                    break;
                case 203 :
                    GlobalData.userInfo = GlobalData.tmpuserInfo;
                    break;

                case 400: break;
                case 401: break;
                case 403: break;
                case 404:
                    GlobalData.findPeer.status = FindPeer.STATUS.NOT_FOUND;
                    MainAddPeerFragment.updateFind();
                    break;
                case 405: break;
                case 407:
                    GlobalData.userInfo.loginCred.isvalid = false;
                    break;
                case 409:
                    GlobalData.userInfo.loginCred.tokenCred.isvalid = false;
                    break;
                case 411: break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onToken(JSONObject obj){
        try {
            String tid = obj.getString("tid");
            String tstr = obj.getString("tstr");
            String uid = obj.getString("uid");

            GlobalData.userInfo.loginCred.isvalid = true;
            GlobalData.userInfo.loginCred.tokenCred.isvalid = true;
            GlobalData.userInfo.loginCred.tokenCred.tid = tid;
            GlobalData.userInfo.loginCred.tokenCred.tstr = tstr;
            GlobalData.userInfo.loginCred.tokenCred.uid = uid;

            GlobalData.SaveUser();

            //Transition To PeerActivity
            if(GlobalData.current != GlobalData.CURRENT.LOGGED) GlobalData.LoginUI();



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onPeerInfo(JSONObject obj){
        try{
            String puid = obj.getString("puid");
            String pname = obj.getJSONObject("pbinfo").getString("name");
            String pabout = obj.getJSONObject("pbinfo").getString("about");


            GlobalData.findPeer.puid = puid;
            GlobalData.findPeer.pname = pname;
            GlobalData.findPeer.pabout = pabout;
            GlobalData.findPeer.status = FindPeer.STATUS.FOUND;
            MainAddPeerFragment.updateFind();


        }catch (Exception e){}
    }

    public void onConnection(JSONObject obj){
        try {
            ;
            //Update Recycler View
            String puid = obj.getString("puid");
            PeerConnection peerConnection = new PeerConnection(new PeerConversation(new PeerInfo(obj)));
            GlobalData.peers.put(puid, peerConnection);
            peerConnection.Connect();
            GlobalData.curractivity.runOnUiThread(PeerActivity::updateList);
        }catch (Exception e){
            GlobalData.showText("Connection Exception : " + e);
        }
    }

    public void onReqPeer(JSONObject obj){
        try{
            //Update Recycler VIew
            String puid = obj.getString("puid");
            PeerConnection peerConnection = new PeerConnection(new PeerConversation(new PeerInfo(obj)));
            String pmsg = obj.getString("pmsg");
            if(pmsg.length() > 0) peerConnection.peerConversation.recvMsg(new TextMessage(false, obj.getString("pmsg")));
            GlobalData.peers.put(puid, peerConnection);
            GlobalData.peers.put(obj.getString("puid"), new PeerConnection(new PeerConversation(new PeerInfo(obj))));
            PeerActivity.updateList();
        }
        catch (Exception e){

        }
    }

}
