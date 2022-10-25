package org.example.service;

import org.example.entity.Record;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class Producer implements Runnable {

    private final BlockingQueue queue;
    private final ExecutorService executorService;
    private final FileAnalyzerService fileAnalyzerService;

    public Producer(BlockingQueue<Record> queue, ExecutorService executorService) {
        this.queue = queue;
        this.executorService = executorService;
        this.fileAnalyzerService = new FileAnalyzerService();
    }

    @Override
    public void run() {
        try (Stream<Path> paths = Files.walk(Path.of("./data"), 1)) {
            paths.filter(Files::isRegularFile)
                .forEach(path -> {
                    Runnable runnable = processPath(path);
                    this.executorService.execute(runnable);
                    System.out.println("Task for " + path.toString() + " was created.");
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Runnable processPath(Path path) {
        return () -> {
            this.fileAnalyzerService.analyze(path).ifPresent(queue::add);
        };
    }

}