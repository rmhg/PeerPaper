package com.rzierfiz.peerpaper.backend.ServerConnection;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.MainThread;

import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Stack;
import java.util.Vector;


public class ServerConnection {
    Socket serverconn = null;
    Thread recvThread = null;
    QueueWorker queueWorker = null;
    Responder res = null;


    public static boolean ConnectionFailed = false;
    boolean isConnected = false;
    boolean isRunning = false;
    private String sip = null;
    private int sport = 0;
    byte[] leftOver = null;

    final int MAX_BUFFER_SIZE = 1024;


    public ServerConnection(String ip, int port) throws IOException {
        serverconn = new Socket();
        sip = ip;sport = port;
        serverconn.setReuseAddress(true);
        queueWorker = new QueueWorker();
        queueWorker.startWorker();
        queueWorker.addWork(this::connect);
        recvThread = new Thread(this::recvIng);

    }

    public void setAddressInfo(String ip, int port){ sip = ip;sport = port; }

    private void connect()  {

        try{
            if (serverconn == null) {
                serverconn = new Socket();
            }else{
                serverconn.close();
                serverconn = new Socket();
            }
            serverconn.setReuseAddress(true);
            serverconn.connect(new InetSocketAddress(sip, sport), 1000);
            if(!isRunning) {
                isRunning = true;
                recvThread.start();
            }
            GlobalData.BindPort = serverconn.getLocalPort();
            isConnected = true;
            ConnectionFailed = false;
        }catch (Exception e){
            GlobalData.current = GlobalData.CURRENT.START;
            GlobalData.onConnectionFailed();
            isConnected = false;
            ConnectionFailed = true;
        }
    }

    public void Connect(){
        queueWorker.addWork(this::connect);
    }
    
    
    
    public void BindResponder(Responder res){
        this.res = res;
    }
    public void sendObj(JSONObject obj) {
        queueWorker.addWork(new Runnable() {
            @Override
            public void run() {
                String data = obj.toString();
                data = data.length() + "\n" + data;
                try {
                    serverconn.getOutputStream().write(data.getBytes());
                }catch (Exception e){
                }
            }
        });
    }

    private void recvIng(){
        while(isRunning){
            recvObj();
        }
    }

    public void stop(){
        GlobalData.current = GlobalData.CURRENT.START;
        isRunning = false;
        try {
            serverconn.close();
        }catch (IOException e){}
        try{
            recvThread.join();
        }catch (Exception e){}
        queueWorker.stopWorker();
    }

    public void stopConn(){
        GlobalData.current = GlobalData.CURRENT.START;
        isRunning = false;
        Log.d("Exp", "About To Close");
        try { serverconn.close();  serverconn = null;}catch (IOException e){}
        Log.d("Exp", "Server Closed, Recv Thread Of ");
        try{ recvThread.join(); }catch (Exception e){}
        Log.d("Exp", "Recv Stoped");
    }

    @Override
    protected void finalize(){
        stop();
    }

    private void recvObj() {
        int readnxt = MAX_BUFFER_SIZE;
        byte[] data = new byte[readnxt];
        JSONObject obj = null;
        String sobj = "";
        int currentobjsize = 0;
        while(readnxt > 0){
            try{
                int rad = serverconn.getInputStream().read(data);
                Log.d("Exp", "Recvied Conn");
                if(rad <= 0) {
                    isRunning = false;
                    return;
                }
                if(leftOver != null){ data = Utils.mergeBytes(leftOver, data); leftOver = null; }
                int i = 0;
                if(currentobjsize > 0){
                    while(i < rad){
                        sobj += (char)data[i++];
                    }
                    break;
                }
                String number = "";
                for(i = 0; i < data.length;i++){
                    if(((char)data[i]) == '\n') break;
                    number += (char)data[i];
                }
                currentobjsize = Integer.parseInt(number);
                i++;
                while(i < rad){
                    sobj += (char)data[i++];
                }
                readnxt = (currentobjsize + number.length() + 1) - rad;
                if(readnxt < 0){
                    leftOver = new byte[-readnxt];
                    int start = i;
                    while(i < rad) leftOver[i - start] = data[i++];

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try{
            obj = new JSONObject(sobj);
            res.Mapper(obj);
        }catch (Exception e){
            queueWorker.addWork(this::stopConn);
        }
    }



}
