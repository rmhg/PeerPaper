package com.rzierfiz.peerpaper.backend.PeerConnection.Discovery;

enum MessageType {
    DISCOVERY(0),
    REQUEST(1),
    REPLY(2),
    ;

    private final int type;

    private MessageType(int type) {
        this.type = type;
    }

}
