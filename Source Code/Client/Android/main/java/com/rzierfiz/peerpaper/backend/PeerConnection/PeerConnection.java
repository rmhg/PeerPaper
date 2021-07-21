package com.rzierfiz.peerpaper.backend.PeerConnection;

import android.os.Build;
import android.util.Log;
import android.util.Pair;

import com.rzierfiz.peerpaper.UI.Peer.PeerActivity;
import com.rzierfiz.peerpaper.UI.Peer.PeerConversation.PeerConversationActivity;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.ServerConnection.QueueWorker;


import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.rzierfiz.peerpaper.backend.PeerConnection.Message.Message;


public class PeerConnection {
    Socket conn = null;
    ServerSocket sconn = null;
    public PeerConversation peerConversation = null;
    boolean isRecving = false;
    Thread recving = new Thread(this::recvIng);
    QueueWorker queueWorker = null;
    boolean tryconnect = false;
    int lport = 0;
    int sended = 0;

    public PeerConnection(PeerConversation peerConversation){
        this.peerConversation = peerConversation;
        queueWorker = new QueueWorker();
        queueWorker.startWorker();
        lport = GlobalData.BindPort;
    }

    public void Connect(){ Log.d("Exp", "Init Connect");queueWorker.addWork(this::doConnect); }

    private void doConnect() {
        if(tryconnect) return;
        int i = 5;
        while(i > 0) {
            if (peerConversation.dst.isconnected || (!peerConversation.dst.isconnectable)) return;
            if (conn == null) {
                if(connect()) break;
                tryconnect = true;
            }
            Log.d("Exp", "Try Again To Connect");
            i--;
        }
        tryconnect = false;
    }
    private boolean connect(){
        try{
            conn = new Socket();
            conn.setReuseAddress(true);
            conn.bind(new InetSocketAddress("0.0.0.0", lport));
            conn.connect(new InetSocketAddress(peerConversation.dst.ip, peerConversation.dst.port));
            peerConversation.dst.isconnected = true;
            isRecving = true;
            tryconnect = false;
            recving.start();
            update();
            return true;
        }catch (Exception e){
            Log.d("Exp", "Connection exp : " + e + " " + Build.MANUFACTURER);
            try{ conn.close(); conn = null;} catch (Exception ex) {}
            //stopConn();
        }
        return false;
    }

    public void stop(){
        queueWorker.stopWorker();
        stopConn();
    }

    public void stopConn(){
        isRecving = false;
        peerConversation.dst.isconnected = false;
        try{ conn.close(); conn = null;}catch(Exception e){}
        try { recving.join(); }catch (Exception e){}
        tryconnect = false;
    }
    private void sendRaw(byte[] data) {
        try {
            conn.getOutputStream().write(data);
        }catch(Exception e){
            peerConversation.dst.isconnected = false;
            update();
            stopConn();
        }
    }
    public void sendMsg(Message peerMessage){

        queueWorker.addWork(()->{ sendRaw(peerMessage.getBytes()); });
        peerConversation.sendMsg(peerMessage);
    }

    private void recvIng(){
        while(isRecving){
            onRecv();
        }
    }

    private static void update(){
        if(GlobalData.curractivity instanceof PeerConversationActivity) GlobalData.curractivity.runOnUiThread(PeerConversationActivity::updateList);
        if(GlobalData.curractivity instanceof PeerActivity) GlobalData.curractivity.runOnUiThread(PeerActivity::updateList);
    }
    private void onRecv() {
        try {
            InputStream is = conn.getInputStream();
            Message message = Message.getMessage(is);
            Log.d("Exp", message.getIsMe() + " Recv");
            peerConversation.recvMsg(message);
            update();
        }catch (Exception e){
            Log.d("Exp", "Exception Recv " + e);
            if(e instanceof SocketException || e instanceof SocketTimeoutException || e instanceof IOException) {
                queueWorker.addWork(this::stopConn);
                update();
            }
        }
    }

}
