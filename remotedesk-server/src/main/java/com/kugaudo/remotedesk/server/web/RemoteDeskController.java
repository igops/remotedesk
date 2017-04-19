package com.kugaudo.remotedesk.server.web;

import com.kugaudo.remotedesk.amqp.mjpeg.messages.ScreenCaptureMessage;
import com.kugaudo.remotedesk.server.amqp.mjpeg.ScreenCaptureReceiver;
import com.kugaudo.remotedesk.server.exceptions.NoSuchClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.kugaudo.remotedesk.conf.RemoteDeskConfiguration.MJPEG_STREAM_FRAME_RATE;

@RestController
public class RemoteDeskController {

    private final ScreenCaptureReceiver receiver;

    @Autowired
    public RemoteDeskController(final ScreenCaptureReceiver receiver) {
        this.receiver = receiver;
    }

    @RequestMapping("/c{id}")
    public StreamingResponseBody stream(@PathVariable("id") byte clientId, HttpServletResponse response) {

        response.setContentType("multipart/x-mixed-replace; boundary=--BoundaryString");

        return os -> {
            while (true) {
                try {
                    final ScreenCaptureMessage message = receiver.getLastMessage(clientId);
                    final byte[] capture = message.getCapture();
                    os.write(("--BoundaryString\r\n" + "Content-type: image/jpeg\r\n" + "Content-Length: "
                            + capture.length + "\r\n\r\n").getBytes());
                    final long started = System.currentTimeMillis();
                    for (final byte b : capture) {
                        os.write(b);
                    }
                    System.out.println("Written in " + (System.currentTimeMillis() - started));
                    os.write("\r\n\r\n".getBytes());
                    os.flush();
                } catch (NoSuchClientException ignored) {}

                try {
                    Thread.sleep(1000 / MJPEG_STREAM_FRAME_RATE);
                } catch (InterruptedException e) {
                    break;
                }
            }
        };
    }
}
