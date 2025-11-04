package com.ainigma100.customerapi.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.StreamSupport;

@Profile("!prod")
@RestController
@RequestMapping("/api")
public class DeveloperPortalController {

    private final Path logsDir = Paths.get("logs");
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Map<SseEmitter, ScheduledFuture<?>> tailTasks = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "log-tail-scheduler");
        t.setDaemon(true);
        return t;
    });



    @GetMapping("/logs/history")
    public String getLogHistory() throws IOException {
        Path latest = findLatestLog();
        return (latest == null) ? "No logs yet." : Files.readString(latest);
    }

    @GetMapping(value = "/logs/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLogs() {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        setupCleanup(emitter);

        Path latest = findLatestLog();
        long startPosition = sendExistingLines(emitter, latest);

        TailState state = new TailState(latest, startPosition);
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(
                () -> tailLogUpdates(emitter, state),
                1, 1, TimeUnit.SECONDS
        );

        tailTasks.put(emitter, task);
        return emitter;
    }

    // ========== Internal Helpers ==========

    private void setupCleanup(SseEmitter emitter) {
        Runnable cleanup = () -> {
            emitters.remove(emitter);
            ScheduledFuture<?> task = tailTasks.remove(emitter);
            if (task != null) task.cancel(true);
        };
        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
    }

    private long sendExistingLines(SseEmitter emitter, Path logFile) {
        if (logFile == null || !Files.exists(logFile)) return 0L;

        try (BufferedReader reader = Files.newBufferedReader(logFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                sendLine(emitter, line);
            }
            return Files.size(logFile); // continue tailing from end
        } catch (IOException e) {
            return 0L;
        }
    }

    private void tailLogUpdates(SseEmitter emitter, TailState state) {
        try {
            Path latest = findLatestLog();
            if (latest == null) return;

            if (state.isNewFile(latest)) {
                state.reset(latest);
            }

            if (!Files.exists(latest)) return;
            long fileLength = Files.size(latest);

            if (fileLength <= state.position) return;

            readNewLines(emitter, state, latest, fileLength);
        } catch (Exception e) {
            closeEmitterWithError(emitter, e);
        }
    }

    private void readNewLines(SseEmitter emitter, TailState state, Path file, long fileLength) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r")) {
            raf.seek(state.position);
            byte[] buffer = new byte[(int) (fileLength - state.position)];
            raf.readFully(buffer);
            state.position = fileLength;

            String chunk = state.leftover + new String(buffer, StandardCharsets.UTF_8);
            state.leftover.setLength(0);

            String[] lines = chunk.replace("\r", "\n").split("\n", -1);
            for (int i = 0; i < lines.length - 1; i++) {
                sendLine(emitter, lines[i]);
            }

            // Handle last (possibly incomplete) line
            String lastLine = lines[lines.length - 1];
            if (chunk.endsWith("\n")) {
                sendLine(emitter, lastLine);
            } else {
                state.leftover.append(lastLine);
            }
        }
    }

    private void sendLine(SseEmitter emitter, String line) {
        try {
            emitter.send(SseEmitter.event().data(line));
        } catch (IOException e) {
            closeEmitterWithError(emitter, e);
        }
    }

    private void closeEmitterWithError(SseEmitter emitter, Exception e) {
        try {
            emitter.completeWithError(e);
        } catch (Exception ignored) {
        }
        ScheduledFuture<?> f = tailTasks.remove(emitter);
        if (f != null) f.cancel(true);
        emitters.remove(emitter);
    }

    private Path findLatestLog() {
        if (!Files.isDirectory(logsDir)) return null;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "app-*.log")) {
            return StreamSupport.stream(stream.spliterator(), false)
                    .filter(Files::isRegularFile)
                    .max(Comparator.comparingLong(p -> {
                        try {
                            return Files.getLastModifiedTime(p).toMillis();
                        } catch (IOException e) {
                            return 0L;
                        }
                    }))
                    .orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    // ========== State Class ==========

    private static class TailState {
        Path current;
        long position;
        final StringBuilder leftover = new StringBuilder();

        TailState(Path file, long pos) {
            this.current = file;
            this.position = pos;
        }

        boolean isNewFile(Path newFile) {
            return current == null || !current.equals(newFile);
        }

        void reset(Path newFile) {
            current = newFile;
            position = 0L;
            leftover.setLength(0);
        }
    }
}
