package com.kugaudo.remotedesk.client.amqp.command;

import com.kugaudo.remotedesk.amqp.command.messages.RemoteCommandMessage;
import org.springframework.stereotype.Component;

@Component
public class RemoteCommandReceiver {

    @SuppressWarnings("unused")
    public void receive(final RemoteCommandMessage message) {
        System.out.println("123");
    }
}
