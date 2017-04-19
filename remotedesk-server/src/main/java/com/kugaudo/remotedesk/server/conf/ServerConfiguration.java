package com.kugaudo.remotedesk.server.conf;

import com.kugaudo.remotedesk.conf.RemoteDeskConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:config.properties")
public class ServerConfiguration extends RemoteDeskConfiguration {

    @Autowired
    public ServerConfiguration(final Environment env) {
        super(env);
    }
}
