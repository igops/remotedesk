package com.kugaudo.remotedesk.amqp.lifecycle.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kugaudo.remotedesk.amqp.lifecycle.LifecycleEventType;

public abstract class LifecycleEventMessage {

    private final LifecycleEventType type;

    @JsonCreator
    protected LifecycleEventMessage(@JsonProperty("type") final LifecycleEventType type) {
        this.type = type;
    }

    public LifecycleEventType getType() {
        return type;
    }
}
