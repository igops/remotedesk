package com.kugaudo.remotedesk.client.conf;

import com.kugaudo.remotedesk.conf.RemoteDeskConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:config.properties")
public class ClientConfiguration extends RemoteDeskConfiguration {

    @Autowired
    public ClientConfiguration(final Environment env) {
        super(env);
    }

    public Integer getRemoteDeskScreenCaptureX() {
        return Integer.valueOf(env.getProperty("remotedesk.screen.capture.x"));
    }

    public Integer getRemoteDeskScreenCaptureY() {
        return Integer.valueOf(env.getProperty("remotedesk.screen.capture.y"));
    }

    public Integer getRemoteDeskScreenCaptureWidth() {
        return Integer.valueOf(env.getProperty("remotedesk.screen.capture.w"));
    }

    public Integer getRemoteDeskScreenCaptureHeight() {
        return Integer.valueOf(env.getProperty("remotedesk.screen.capture.h"));
    }
}
