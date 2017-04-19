package com.kugaudo.remotedesk.amqp.mjpeg.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScreenCaptureMessage {

    private byte clientId;
    private byte[] capture;

    @JsonCreator
    public ScreenCaptureMessage(@JsonProperty("clientId") final byte clientId,
                                @JsonProperty("capture") final byte[] capture) {
        this.clientId = clientId;
        this.capture = capture;
    }

    public byte getClientId() {
        return clientId;
    }

    public byte[] getCapture() {
        return capture;
    }
}
