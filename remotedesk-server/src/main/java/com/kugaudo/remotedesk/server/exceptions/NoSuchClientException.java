package com.kugaudo.remotedesk.server.exceptions;

public class NoSuchClientException extends Exception {

    public NoSuchClientException(final byte clientId) {
        super("The client [" + clientId + "] is not registered");
    }
}
