package com.ainigma100.customerapi.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Profile("!prod")
@RestController
@RequestMapping("/api")
public class DeveloperPortalController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Path logFile = Paths.get("logs/app.log");

    @GetMapping("/logs/history")
    public String getLogHistory() throws IOException {
        if (!Files.exists(logFile)) return "No logs yet.";
        return Files.readString(logFile);
    }

    @GetMapping(value = "/logs/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLogs() throws IOException {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        // Send current last lines (optional)
        if (Files.exists(logFile)) {
            try (BufferedReader reader = Files.newBufferedReader(logFile)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    emitter.send(SseEmitter.event().data(line));
                }
            }
        }

        // Start watching for new log lines
        startFileWatcher();

        return emitter;
    }

    private void startFileWatcher() {
        new Thread(() -> {
            try {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                logFile.getParent().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    var key = watcher.take();
                    for (var event : key.pollEvents()) {
                        if (event.context().toString().equals(logFile.getFileName().toString())) {
                            try (RandomAccessFile raf = new RandomAccessFile(logFile.toFile(), "r")) {
                                String line;
                                while ((line = raf.readLine()) != null) {
                                    for (var emitter : emitters) {
                                        try {
                                            emitter.send(SseEmitter.event().data(line));
                                        } catch (IOException e) {
                                            emitters.remove(emitter);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    key.reset();
                }
            } catch (Exception ignored) {}
        }).start();
    }
}
