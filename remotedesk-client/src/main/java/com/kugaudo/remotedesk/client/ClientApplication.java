package com.kugaudo.remotedesk.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kugaudo.remotedesk.amqp.lifecycle.messages.RegisterClientMessage;
import com.kugaudo.remotedesk.conf.RemoteDeskConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClientApplication {

    private static byte clientId;

    private final RabbitTemplate rabbitTemplate;
    private final RemoteDeskConfiguration configuration;
    private final ObjectMapper objectMapper;

    @Autowired
    public ClientApplication(final RabbitTemplate rabbitTemplate, final RemoteDeskConfiguration configuration) {
        this.rabbitTemplate = rabbitTemplate;
        this.configuration = configuration;
        this.objectMapper = new ObjectMapper();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientApplication.class).web(false).run(args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> init(Byte.valueOf(args[0]));
    }

    public static byte getClientId() {
        return clientId;
    }

    private void init(final byte clientId) {
        ClientApplication.clientId = clientId;

        final RegisterClientMessage message = new RegisterClientMessage(clientId);
        try {
            rabbitTemplate.convertAndSend(configuration.getLifecycleEventQueueName(), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}