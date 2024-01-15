package com.example.SpotifyToYoutube;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;

@RestController
@Slf4j
public class SseController {

    private SseEmitter sseEmitter;

    @PostConstruct
    public void initializeSseEmitter() {
        sseEmitter = createSseEmitter();
    }

    private SseEmitter createSseEmitter() {
        SseEmitter emitter = new SseEmitter();
        emitter.onCompletion(() -> log.info("SseEmitter is completed"));
        emitter.onTimeout(() -> log.info("SseEmitter is timed out"));
        emitter.onError((ex) -> log.info("SseEmitter got error:", ex));
        return emitter;
    }

    @GetMapping(path = "/api/live/events", produces = "text/event-stream")
    public SseEmitter sendLiveUpdate() {
        sseEmitter.onCompletion(() -> log.info("SseEmitter is completed"));
        sseEmitter.onTimeout(() -> log.info("SseEmitter is timed out"));
        sseEmitter.onError((ex) -> log.info("SseEmitter got error:", ex));
        return sseEmitter;
    }

    // You can create another method to send updates to the client
    public void updateClient(String message) {
        System.out.println("SSE: Sending " + SseEmitter.event().name("update").data(message));
        try {
            sseEmitter.send(SseEmitter.event().name("update").data(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
