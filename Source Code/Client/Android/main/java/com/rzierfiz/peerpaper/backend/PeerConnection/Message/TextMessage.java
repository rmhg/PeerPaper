package com.rzierfiz.peerpaper.backend.PeerConnection.Message;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;

import static com.rzierfiz.peerpaper.backend.PeerConnection.Message.MetaType.Payload;

public class TextMessage extends Message {

    public TextMessage(){
        setPayloadType(PayloadType.TEXT);
        setEncoding(EncodingType.TEXT_UTF8);
    }

    public TextMessage(boolean isme){
        setIsMe(isme);
        setPayloadType(PayloadType.TEXT);
        setEncoding(EncodingType.TEXT_UTF8);
    }

    public TextMessage(boolean isme, String text) {
        setIsMe(isme);
        setPayloadType(PayloadType.TEXT);
        setEncoding(EncodingType.TEXT_UTF8);
        setPayloadData(text.getBytes(), text.length());
    }

    public void setText(String text) {
        setPayloadData(text.getBytes(), text.length());
    }

    public static TextMessage fromMessage(Message message){
        TextMessage textMessage = new TextMessage(message.getIsMe());
        textMessage.setPayloadData(message.getPayloadData(), message.getPayloadLength());
        return textMessage;
    }


    public String getText() {
        String msg = "";
        for (byte ch : getBytes()) msg += (char) ch;
        return msg;
    }

}

