package com.rzierfiz.peerpaper.backend.PeerConnection.Message;


import android.util.Log;

import com.rzierfiz.peerpaper.backend.Global.GlobalData;

import java.io.InputStream;


public class Message{

    EncodingType encodingType;
    PayloadType payloadType;
    boolean isme = true;
    byte[] payloadData = null;
    int plen = 0;

    public Message() {}

    public void setEncoding(EncodingType eType) { encodingType = eType; }
    public void setPayloadType(PayloadType pType) { payloadType = pType; }
    public void setPayloadData(byte[] data, int len) { this.payloadData = data; this.plen = len; }
    public void setIsMe(boolean isme){ this.isme = isme; }

    public EncodingType getEncoding() { return encodingType; }
    public PayloadType getPayloadType() { return payloadType; }
    public int getPayloadLength() { return plen; }
    public boolean getIsMe() { return isme; }
    public byte[] getPayloadData(){ return payloadData; }

    public byte[] getBytes() {
        /*TLV*/
        /*MetaType[1], Length[1], PayloadType[1], EncodingType[1], EXTRA[4]*/
        /*MetaType[1], Length[4], VALUE*/

        byte[] msg = new byte[8 + 5 + plen + 1];

        msg[0] = (byte)MetaType.PayloadType.ordinal();
        msg[1] = (byte)6;
        msg[2] = (byte)payloadType.ordinal();
        msg[3] = (byte)encodingType.ordinal();
        msg[4] = (byte)0;msg[5] = (byte)0;msg[6] = (byte)0;msg[7] = (byte)0;

        msg[8] = (byte)MetaType.Payload.ordinal();
        byte[] bplen = new byte[4];

        int tplen = plen;
        for(int i = 0;i < 4;i++, tplen >>= 8 ) bplen[i] = (byte)tplen;

        for(int i = 0;i < 4;i++) msg[9 + i] = bplen[i];
        for(int i = 0;i < plen;i++) msg[9 + 4 + i] = payloadData[i];

        msg[9 + 4 + plen] = (byte)MetaType.END.ordinal();

        Log.d("Exp", plen + " Send Len");
        return msg;

    }


    public static Message getMessage(InputStream is) throws Exception{
        Message message = new Message();
        message.setIsMe(false);
        int val = is.read();
        MetaType mtype = MetaType.values()[val];
        int len = 0;
        while(mtype != MetaType.END){
            switch (mtype){
                case PayloadType:
                    len = is.read();
                    byte[] data = new byte[len];
                    is.read(data);
                    PayloadType payloadType = PayloadType.values()[(int)data[0]];
                    EncodingType encodingType = EncodingType.values()[(int)data[1]];
                    Log.d("Exp", mtype + "" + len + " " + payloadType + " " + encodingType);
                    message.setPayloadType(payloadType);
                    message.setEncoding(encodingType);
                    break;
                case Payload:
                    byte[] blen = new byte[4];
                    is.read(blen);
                    len = 0;
                    for(int i = 3; i >= 0;i--){
                        len <<= 8;
                        len |= (0x0000000ff) & blen[i];
                    }
                    byte[] payloadData = new byte[len];
                    int x = 0;
                    int s = 0;
                    int rm = len - x;
                    while(rm > 0){
                        Log.d("Exp", "Offset : " + x + " , Len : " + rm);
                        x = is.read(payloadData, x, rm); s += x;
                        rm = rm - x;
                    }
                    Log.d("Exp", mtype + " " + len + " " + x + " " + s);
                    message.setPayloadData(payloadData, len);
                    break;
            }
            Log.d("Exp", "MetaType : " + mtype);
            mtype = MetaType.values()[is.read()];
        }
        return message;
    }

    public static Message getMessage(byte[] data) {
        try {
            Message msg = new Message();
            msg.setIsMe(false);
            byte[] payloadData = null;
            int plen = 0;
            int index = 0;
            int len = 0;

            while(index < data.length) {
                MetaType metaType = MetaType.values()[(int)data[index]];
                switch(metaType) {
                    case PayloadType:
                        len = data[index + 1];
                        PayloadType payloadType = PayloadType.values()[(int)data[index + 2]];
                        EncodingType encodingType = EncodingType.values()[(int)data[index + 3]];
                        msg.setPayloadType(payloadType);
                        msg.setEncoding(encodingType);
                        len += 1;
                        break;
                    case Payload:
                        len = 0;
                        for(int i = 3;i >= 0;i--) {
                            len <<= 8;
                            len |= (int)(0x000000ff & data[index + i + 1]);
                        }
                        plen = len;
                        Log.d("Exp", "" + plen + " len");
                        GlobalData.showTextShort(plen + "");
                        payloadData = new byte[plen];
                        for(int i = 0;i < plen;i++) {
                            payloadData[i] = data[i + index + 4 + 1];
                        }
                        msg.setPayloadData(payloadData, plen);
                        len += 4;
                        break;
                    case END: index = data.length + 1; break;
                }
                index += len + 1;
            }
            return msg;
        }catch(Exception e) {
            Log.d("Exp", "" + e);
        }
        return null;
    }



}


