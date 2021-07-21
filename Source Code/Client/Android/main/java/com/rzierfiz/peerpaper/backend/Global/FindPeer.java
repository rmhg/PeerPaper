package com.rzierfiz.peerpaper.backend.Global;

public class FindPeer {
    public enum STATUS{
      NOT_FOUND,
      FOUND,
        UNSET
    };
    public String puid = "", pname = "", pabout = "";
    public STATUS status = STATUS.UNSET;
}
