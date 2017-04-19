package com.kugaudo.remotedesk.client.conf;

import com.kugaudo.remotedesk.client.amqp.command.RemoteCommandReceiver;
import com.kugaudo.remotedesk.conf.RemoteDeskConfiguration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@Import(com.kugaudo.remotedesk.conf.AppConfig.class)
public class AppConfig {

    @Bean
    @Qualifier("RemoteCommandListenerAdapter")
    MessageListenerAdapter remoteCommandListenerAdapter(RemoteCommandReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receive");
    }

    @Bean
    SimpleMessageListenerContainer remoteCommandListenerContainer(ConnectionFactory connectionFactory,
                                                                  @Qualifier("RemoteCommandListenerAdapter") MessageListenerAdapter listenerAdapter,
                                                                  RemoteDeskConfiguration configuration) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(configuration.getRemoteCommandQueueName());
        container.setMessageListener(listenerAdapter);
        return container;
    }

}
