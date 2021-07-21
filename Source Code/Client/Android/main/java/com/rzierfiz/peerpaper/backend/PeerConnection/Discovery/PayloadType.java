package com.rzierfiz.peerpaper.backend.PeerConnection.Discovery;

enum PayloadType {
    INFO(0),
    CONNECT(1),
    DENY(2),
    ;
    private final int type;

    private PayloadType(int type) {
        this.type = type;
    }

}
