package com.rzierfiz.peerpaper.backend.PeerConnection.Message;

import java.io.FileInputStream;

public class ImageMessage extends Message {

    String path = null;

    public ImageMessage(){
        setPayloadType(PayloadType.IMG);
    }

    public ImageMessage(boolean isme, String aPath) {
        setIsMe(isme);
        setPayloadType(PayloadType.IMG);
        LoadData();
    }

    public ImageMessage(boolean isme, byte[] data) {
        setIsMe(isme);
        setPayloadType(PayloadType.IMG);
        setEncoding(EncodingType.IMG_RAW);
        setPayloadData(data, data.length);
    }

    void LoadData() {
        byte[] data = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            int len = fileInputStream.available();
            if (len > 0) {
                data = new byte[len];
                fileInputStream.read(data);
            }
            fileInputStream.close();
        } catch (Exception e) {
        }
        if (path.endsWith("jpg") || path.endsWith("jpeg")) {
            setEncoding(EncodingType.IMG_JPG);
        } else if (path.endsWith("png")) {
            setEncoding(EncodingType.IMG_PNG);
        }
        setPayloadData(data, data.length);

    }

}
