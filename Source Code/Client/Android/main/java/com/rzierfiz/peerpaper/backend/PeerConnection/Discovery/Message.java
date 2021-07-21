package com.rzierfiz.peerpaper.backend.PeerConnection.Discovery;

import android.util.Log;

import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.ServerConnection.QueueWorker;
import com.rzierfiz.peerpaper.backend.Utils;

public class Message {
    public MessageType mType;
    public PayloadType pType = PayloadType.DENY;
    public int length = 0;
    public byte[] payload = null;
    final int MT_MAX = 3;
    final int PT_MAX = 2;


    public static Message getMessage(byte[] raw) {
        Message message = new Message();
        try {
            message.mType = MessageType.values()[(int) raw[0]];
            message.pType = PayloadType.values()[(int) raw[1]];
            message.length = (int) raw[2];
            message.payload = new byte[message.length];
            for (int i = 3; i < message.length + 3; i++) message.payload[i - 3] = raw[i];
            return message;
        } catch (Exception e) {
        }
        return null;
    }

    public byte[] getRaw() {
        byte[] raw = new byte[length + 3];
        raw[0] = (byte) mType.ordinal();
        raw[1] = (byte) pType.ordinal();
        raw[2] = (byte) length;
        for (int i = 3; i < length + 3; i++) raw[i] = payload[i - 3];
        return raw;
    }

    public static Message getMessage(MessageType messageType, PayloadType payloadType) {
        Message message = new Message();
        message.mType = messageType;
        message.pType = payloadType;
        if (payloadType != PayloadType.DENY) {
            byte[] pport = Utils.fromInt(GlobalData.BindPort);
            String uid = GlobalData.userInfo.loginCred.uid.length() > 0? GlobalData.userInfo.loginCred.uid : "PeerPaper";
            byte[] uidb = (uid + "\0").getBytes();
            message.payload = Utils.mergeBytes(pport, uidb);
            message.length = message.payload.length;
        }
        return message;
    }
}
