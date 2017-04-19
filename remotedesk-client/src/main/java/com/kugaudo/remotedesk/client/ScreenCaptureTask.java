package com.kugaudo.remotedesk.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kugaudo.remotedesk.amqp.mjpeg.messages.ScreenCaptureMessage;
import com.kugaudo.remotedesk.client.conf.ClientConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.kugaudo.remotedesk.conf.RemoteDeskConfiguration.CAPTURE_FRAME_RATE;
import static com.kugaudo.remotedesk.conf.RemoteDeskConfiguration.CAPTURE_INITIAL_DELAY_MS;

@Component
public class ScreenCaptureTask {

    static {
        System.setProperty("java.awt.headless", "false");
    }

    private final Robot robot;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    private final ClientConfiguration configuration;

    @Autowired
    public ScreenCaptureTask(final RabbitTemplate rabbitTemplate, final ClientConfiguration configuration) throws AWTException {
        this.robot = new Robot();
        this.rabbitTemplate = rabbitTemplate;
        this.configuration = configuration;
        this.objectMapper = new ObjectMapper();
    }

    @Scheduled(fixedRate = 1000 / CAPTURE_FRAME_RATE, initialDelay = CAPTURE_INITIAL_DELAY_MS)
    public void capture() {
        final int x = configuration.getRemoteDeskScreenCaptureX();
        final int y = configuration.getRemoteDeskScreenCaptureY();
        final int width = configuration.getRemoteDeskScreenCaptureWidth();
        final int height = configuration.getRemoteDeskScreenCaptureHeight();
        final Rectangle screenRect = new Rectangle(x, y, width, height);

        final BufferedImage capture = robot.createScreenCapture(screenRect);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(capture, "jpg", baos);
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (baos.size() != 0) {
            final ScreenCaptureMessage message = new ScreenCaptureMessage(ClientApplication.getClientId(), baos.toByteArray());
            try {
                rabbitTemplate.convertAndSend(configuration.getScreenCaptureQueueName(), objectMapper.writeValueAsString(message));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
