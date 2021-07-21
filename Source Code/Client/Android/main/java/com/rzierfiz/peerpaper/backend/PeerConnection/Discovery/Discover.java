package com.rzierfiz.peerpaper.backend.PeerConnection.Discovery;

import android.util.Log;

import com.rzierfiz.peerpaper.UI.Peer.AddPeer.Discover.DiscoverAdapter;
import com.rzierfiz.peerpaper.UI.Peer.AddPeer.DiscoverAddPeerFragment;
import com.rzierfiz.peerpaper.UI.Peer.PeerActivity;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.Global.LoginCred;
import com.rzierfiz.peerpaper.backend.Global.PeerInfo;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConnection;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConversation;
import com.rzierfiz.peerpaper.backend.ServerConnection.QueueWorker;
import com.rzierfiz.peerpaper.backend.Utils;

import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.interfaces.DSAKeyPairGenerator;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;


/*
* UNIQUE THE PUID AND PRESERVE IT
*
* */

public class Discover {

    DatagramSocket udp = null;
    boolean isRecving = false;
    boolean isBinded = false;
    Thread recvIng = new Thread(this::recv);
    QueueWorker queueWorker = null;
    ArrayList<InetAddress> inetAddresses = new ArrayList<>();

    public Discover() {
        Bind();
        try {
            Enumeration ne = NetworkInterface.getNetworkInterfaces();
            while (ne.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) ne.nextElement();
                List<InterfaceAddress> lia = ni.getInterfaceAddresses();
                for (InterfaceAddress interfaceAddress : lia) {
                    if (interfaceAddress.getBroadcast() != null) {
                        inetAddresses.add (interfaceAddress.getBroadcast());
                    }
                }
            }
        } catch (SocketException e) {
            Log.d("Exp", e + "");
            e.printStackTrace();
        }
    }

    private void Bind(){
        try {
            udp = new DatagramSocket(GlobalData.disBindPort);
            udp.setBroadcast(true);
            queueWorker = new QueueWorker();
            queueWorker.startWorker();
            isBinded = true;
            start();

        }catch (Exception e){
            isBinded = false;
        }
    }

    public static boolean isLocal(InetAddress ip){
        try {
            Enumeration ne = NetworkInterface.getNetworkInterfaces();
            while(ne.hasMoreElements()){
                NetworkInterface ni = (NetworkInterface) ne.nextElement();
                Enumeration ine = ni.getInetAddresses();
                while(ine.hasMoreElements()){
                    InetAddress ia = (InetAddress)ine.nextElement();
                    if(ip.getHostAddress().equals(ia.getHostAddress())){
                       return true;
                   }
                }

            }
        } catch (SocketException e) {
            Log.d("Exp", e + "");
            e.printStackTrace();
        }
        return false;
    }


    public void start(){
        if(isBinded && !isRecving){
            isRecving = true;
            recvIng.start();
        }
    }

    public void Discover(){
        SendAll(Message.getMessage(MessageType.DISCOVERY, PayloadType.INFO));
    }

    public void Request(String puid){
        DiscoverPeerInfo discoverPeerInfo = GlobalData.dpeers.get(puid);
        discoverPeerInfo.request = true;
        try {
            Send(InetAddress.getByName(discoverPeerInfo.ip), GlobalData.disBindPort, Message.getMessage(MessageType.REQUEST, PayloadType.CONNECT));
        }catch (Exception e){}
    }

    private void Send(InetAddress ip, int port, Message message){
        queueWorker.addWork(()-> {
            byte[] data = message.getRaw();
            int len = data.length;
            DatagramPacket dp = new DatagramPacket(data, len, ip, port);
            try {
                udp.send(dp);
            } catch (Exception e) {
            }
        });
    }

    private void SendAll(Message message){
        queueWorker.addWork(()-> {
            try {
                InetAddress ip = InetAddress.getByAddress(new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff});
                byte[] data = message.getRaw();
                int len = data.length;
                DatagramPacket dp = new DatagramPacket(data, len, ip, GlobalData.disBindPort);
                for(InetAddress ia : inetAddresses) {
                    dp.setAddress(ia);
                    udp.send(dp);
                }
            } catch (Exception e) {
            }
        });
    }

    void onRecv(InetAddress ip, int port, Message message){
        MessageType messageType = message.mType;
        PayloadType payloadType = message.pType;
        switch (messageType){
            case DISCOVERY:
                if(payloadType == PayloadType.INFO) {
                    DiscoverPeerInfo discoverPeerInfo = DiscoverPeerInfo.fromPayload(message.payload);
                    discoverPeerInfo.ip = ip.getHostAddress();
                    discoverPeerInfo.puid = discoverPeerInfo.ip;
                    DiscoverPeerInfo dpinfo = GlobalData.dpeers.get(discoverPeerInfo.ip);
                    if(dpinfo == null) SendAll(Message.getMessage(MessageType.DISCOVERY, PayloadType.INFO));
                    GlobalData.dpeers.put(discoverPeerInfo.ip, discoverPeerInfo);
                    DiscoverAdapter.update();
                }else if(payloadType == PayloadType.DENY){
                    GlobalData.dpeers.remove(ip.getHostAddress());
                    DiscoverAdapter.update();
                }
                break;
            case REQUEST:
                if(payloadType == PayloadType.INFO) {

                }else if(payloadType == PayloadType.CONNECT){
                    DiscoverPeerInfo dpinfo = GlobalData.dpeers.get(ip.getHostAddress());
                    dpinfo.isrequested = true;
                    if(dpinfo.isrequested && dpinfo.request){
                        PeerInfo peerInfo = dpinfo.getPeerInfo();
                        peerInfo.puid = dpinfo.ip;
                        PeerConnection peerConnection = GlobalData.peers.get(peerInfo.puid);
                        if(peerConnection == null){
                            peerConnection = new PeerConnection(new PeerConversation(peerInfo));
                            GlobalData.peers.put(peerInfo.puid, peerConnection);
                            PeerActivity.updateList();
                        }else{
                            //peerConnection.stopConn();
                        }
                        Send(ip, port, Message.getMessage(MessageType.REPLY, PayloadType.CONNECT));
                        peerConnection.Connect();
                    }
                }
                break;
            case REPLY:
                if(payloadType == PayloadType.CONNECT){
                    DiscoverPeerInfo discoverPeerInfo = DiscoverPeerInfo.fromPayload(message.payload);
                    discoverPeerInfo.ip = ip.getHostAddress();
                    discoverPeerInfo.puid = discoverPeerInfo.ip;
                    PeerInfo peerInfo = discoverPeerInfo.getPeerInfo();
                    peerInfo.puid = peerInfo.ip;
                    PeerConnection peerConnection = GlobalData.peers.get(peerInfo.puid);
                    if(peerConnection == null) {
                        peerConnection = new PeerConnection(new PeerConversation(peerInfo));
                        GlobalData.peers.put(peerInfo.ip, peerConnection);
                        PeerActivity.updateList();
                    }else{
                        //peerConnection.stopConn();
                    }
                    peerConnection.Connect();
                }
        }
    }



    private void recv(){
        while(isRecving){
            try {
                byte[] raw = new byte[1024];
                DatagramPacket dp = new DatagramPacket(raw, 1024);
                udp.receive(dp);
                if(!isLocal(dp.getAddress())){
                    onRecv(dp.getAddress(), dp.getPort(), Message.getMessage(dp.getData()));
                }
            }catch (Exception e){
            }

        }
    }


}
