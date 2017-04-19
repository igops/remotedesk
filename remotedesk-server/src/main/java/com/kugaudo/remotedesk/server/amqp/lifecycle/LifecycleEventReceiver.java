package com.kugaudo.remotedesk.server.amqp.lifecycle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kugaudo.remotedesk.amqp.lifecycle.messages.RegisterClientMessage;
import com.kugaudo.remotedesk.server.amqp.mjpeg.ScreenCaptureReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LifecycleEventReceiver {

    private final ScreenCaptureReceiver screenCaptureReceiver;
    private final ObjectMapper objectMapper;

    @Autowired
    public LifecycleEventReceiver(final ScreenCaptureReceiver screenCaptureReceiver) {
        this.screenCaptureReceiver = screenCaptureReceiver;
        this.objectMapper = new ObjectMapper();
    }

    // TODO receive multiple types ?
    @SuppressWarnings("unused")
    public void receive(final String message) {
        try {
            RegisterClientMessage msg = objectMapper.readValue(message, RegisterClientMessage.class);
            screenCaptureReceiver.registerClient(msg.getClientId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
