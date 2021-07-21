package com.rzierfiz.peerpaper.backend.PeerConnection;

import android.util.Log;

import com.rzierfiz.peerpaper.backend.Global.PeerInfo;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.Message;

import java.util.Vector;


public class PeerConversation {
    public PeerInfo dst = null;
    public Vector<Message> msgs = new Vector<>();

    public PeerConversation(PeerInfo dst){ this.dst = dst; }
    public void sendMsg(Message peerMessage) { msgs.add(peerMessage); }
    public void recvMsg(Message peerMessage){
        Log.d("Exp", "Recv " + peerMessage.getPayloadType());
        msgs.add(peerMessage); }


}
