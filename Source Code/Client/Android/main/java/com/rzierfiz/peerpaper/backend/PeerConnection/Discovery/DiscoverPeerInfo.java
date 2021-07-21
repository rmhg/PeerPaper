package com.rzierfiz.peerpaper.backend.PeerConnection.Discovery;

import com.rzierfiz.peerpaper.backend.Global.PeerInfo;
import com.rzierfiz.peerpaper.backend.Utils;

public class DiscoverPeerInfo {
    public String ip = "";
    public int port = 0;
    public int uport = 0;
    public String puid = "";
    public boolean request = false;
    public boolean isrequested = false;


    public static DiscoverPeerInfo fromPayload(byte[] payload) {
        DiscoverPeerInfo dpeerinfo = new DiscoverPeerInfo();
        dpeerinfo.ip = "";
        dpeerinfo.port = Utils.fromBytes(payload);
        for (int i = 4; i < payload.length; i++) {
            char ch = (char) payload[i];
            if (ch == '\0') break;
            dpeerinfo.puid += ch;
        }
        return dpeerinfo;
    }

    public PeerInfo getPeerInfo() {
        PeerInfo peerinfo = new PeerInfo();
        peerinfo.isvalid = true;
        peerinfo.isconnectable = true;
        peerinfo.port = port;
        peerinfo.puid = puid;
        peerinfo.ip = ip;
        return peerinfo;
    }

}
