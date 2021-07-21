package com.rzierfiz.peerpaper.backend;

import android.util.Log;

public class Utils{

    public static byte[] mergeBytes(byte[] first, byte[] second) {
        byte[] merged = new byte[first.length + second.length];
        for(int i = 0; i < merged.length;i++){
            if(i < first.length) merged[i] = first[i];
            else merged[i] = second[i - first.length];
        }
        return merged;
    }

    public static String byte2str(byte[] bytes){
        String str = "";
        for(byte k : bytes) {
            char ch = (char)k;
            str += ch;
        }
        return str;
    }

    public static int fromBytes(byte[] intb) {
        int ib = 0;
        for(int i = 3;i >= 0;i--) {
            ib = ib << 8;
            ib = ib | (0x00ff & (intb[i]));

        }
        return ib;
    }

    public static byte[] fromInt(int ib) {
        byte[] intb = new byte[4];
        for(int i = 0;i < 4;i++) {
            intb[i] = 0;
            intb[i] = (byte)(ib);
            ib = ib >> 8;
        }
        return intb;
    }

}
