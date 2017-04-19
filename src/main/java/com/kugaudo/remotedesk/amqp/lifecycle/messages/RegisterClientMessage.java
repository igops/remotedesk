package com.kugaudo.remotedesk.amqp.lifecycle.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kugaudo.remotedesk.amqp.lifecycle.LifecycleEventType;

public class RegisterClientMessage extends LifecycleEventMessage {

    private final byte clientId;

    public RegisterClientMessage(@JsonProperty("clientId") final byte clientId) {
        super(LifecycleEventType.REGISTER_CLIENT);
        this.clientId = clientId;
    }

    public byte getClientId() {
        return clientId;
    }
}
