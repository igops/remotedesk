package com.kugaudo.remotedesk.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
public class AppConfig {

    @Bean
    TopicExchange exchange(RemoteDeskConfiguration configuration) {
        return new TopicExchange(configuration.getTopicExchangeName());
    }

    @Bean
    @Qualifier("screenCaptureQueue")
    Queue screenCaptureQueue(RemoteDeskConfiguration configuration) {
        return new Queue(configuration.getScreenCaptureQueueName(), false);
    }

    @Bean
    @Qualifier("remoteCommandQueue")
    Queue remoteCommandQueue(RemoteDeskConfiguration configuration) {
        return new Queue(configuration.getRemoteCommandQueueName(), false);
    }

    @Bean
    @Qualifier("lifecycleEventQueue")
    Queue lifecycleEventQueue(RemoteDeskConfiguration configuration) {
        return new Queue(configuration.getLifecycleEventQueueName(), false);
    }

    @Bean
    Binding screenCaptureBinding(@Qualifier("screenCaptureQueue") Queue queue, TopicExchange exchange, RemoteDeskConfiguration configuration) {
        return BindingBuilder.bind(queue).to(exchange).with(configuration.getScreenCaptureQueueName());
    }

    @Bean
    Binding remoteCommandBinding(@Qualifier("remoteCommandQueue") Queue queue, TopicExchange exchange, RemoteDeskConfiguration configuration) {
        return BindingBuilder.bind(queue).to(exchange).with(configuration.getRemoteCommandQueueName());
    }

    @Bean
    Binding lifecycleEventBinding(@Qualifier("lifecycleEventQueue") Queue queue, TopicExchange exchange, RemoteDeskConfiguration configuration) {
        return BindingBuilder.bind(queue).to(exchange).with(configuration.getLifecycleEventQueueName());
    }
}