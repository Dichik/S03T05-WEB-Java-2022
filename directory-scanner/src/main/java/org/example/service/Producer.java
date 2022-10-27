package org.example.service;

import org.example.entity.Record;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class Producer implements Runnable {

    private final BlockingQueue<FileAnalyzingTask> queue;

    public Producer(BlockingQueue<FileAnalyzingTask> queue) {
        this.queue = queue;
        // TODO use hashmap to check which path we already checked
    }

    @Override
    public void run() {
        try (Stream<Path> paths = Files.walk(Path.of("./data"), 1)) {
            paths.filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        this.queue.put(new FileAnalyzingTask(path));
                    } catch (InterruptedException e) {
                        System.err.println("Can't create task for " + path.toString() + " Error: " + e);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}