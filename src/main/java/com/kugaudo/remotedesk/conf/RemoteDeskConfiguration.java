package com.kugaudo.remotedesk.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class RemoteDeskConfiguration {

    public static final int CAPTURE_INITIAL_DELAY_MS = 100;
    public static final int CAPTURE_FRAME_RATE = 24;
    public static final int MJPEG_STREAM_FRAME_RATE = 24;

    protected final Environment env;

    @Autowired
    public RemoteDeskConfiguration(final Environment env) {
        this.env = env;
    }

    public String getScreenCaptureQueueName() {
        return env.getProperty("amqp.queue.screencapture.name");
    }

    public String getLifecycleEventQueueName() {
        return env.getProperty("amqp.queue.lifecycleevents.name");
    }

    public String getRemoteCommandQueueName() {
        return env.getProperty("amqp.queue.remotecommands.name");
    }

    public String getTopicExchangeName() {
        return env.getProperty("amqp.exchange.name");
    }

}
