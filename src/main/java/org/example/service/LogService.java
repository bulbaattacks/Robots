package org.example.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class LogService {
    private static final LogService instance = new LogService();

    private LogService() {}

    public static LogService getInstance() {return instance;}

    private final Set<SseEmitter> emitters = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    public void log(String log) {
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("log-event")
                        .data(log));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        });
    }
}