package com.kugaudo.remotedesk.server.conf;

import com.kugaudo.remotedesk.conf.RemoteDeskConfiguration;
import com.kugaudo.remotedesk.server.amqp.lifecycle.LifecycleEventReceiver;
import com.kugaudo.remotedesk.server.amqp.mjpeg.ScreenCaptureReceiver;
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
    @Qualifier("ScreenCaptureListenerAdapter")
    MessageListenerAdapter screenCaptureListenerAdapter(ScreenCaptureReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receive");
    }

    @Bean
    @Qualifier("LifecycleEventListenerAdapter")
    MessageListenerAdapter lifecycleEventListenerAdapter(LifecycleEventReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receive");
    }

    @Bean
    @Qualifier("ScreenCaptureListenerContainer")
    SimpleMessageListenerContainer screenCaptureListenerContainer(ConnectionFactory connectionFactory,
                                                                  @Qualifier("ScreenCaptureListenerAdapter") MessageListenerAdapter listenerAdapter,
                                                                  RemoteDeskConfiguration configuration) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(configuration.getScreenCaptureQueueName());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    @Qualifier("LifecycleEventListenerContainer")
    SimpleMessageListenerContainer lifecycleEventListenerContainer(ConnectionFactory connectionFactory,
                                             @Qualifier("LifecycleEventListenerAdapter") MessageListenerAdapter listenerAdapter,
                                             RemoteDeskConfiguration configuration) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(configuration.getLifecycleEventQueueName());
        container.setMessageListener(listenerAdapter);
        return container;
    }

}