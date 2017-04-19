package com.kugaudo.remotedesk.server.amqp.mjpeg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kugaudo.remotedesk.amqp.mjpeg.messages.ScreenCaptureMessage;
import com.kugaudo.remotedesk.server.exceptions.NoSuchClientException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class ScreenCaptureReceiver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Set<Byte> clients = Collections.synchronizedSet(new HashSet<>());
    private Map<Byte, ScreenCaptureMessage> lastMessage = new HashMap<>();

    private long lastMessageTime = 0;

    @SuppressWarnings("unused")
    public void receive(String message) {
        try {
            //debug();
            final ScreenCaptureMessage msg = objectMapper.readValue(message, ScreenCaptureMessage.class);
            final byte clientId = msg.getClientId();
            try {
                checkIfClientRegistered(clientId);
                lastMessage.put(clientId, msg);
            } catch (NoSuchClientException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void debug() {
        final long now = System.currentTimeMillis();
        System.out.println("Capture message latency: " + (now - lastMessageTime));
        lastMessageTime = now;
    }

    public void registerClient(final byte clientId) {
        clients.add(clientId);
    }

    public ScreenCaptureMessage getLastMessage(final byte clientId) throws NoSuchClientException {
        checkIfClientRegistered(clientId);
        return lastMessage.get(clientId);
    }

    private void checkIfClientRegistered(final byte clientId) throws NoSuchClientException {
        if (!clients.contains(clientId)) {
            throw new NoSuchClientException(clientId);
        }
    }
}
