package org.example.producer;

import org.example.entity.Record;
import org.example.service.FileAnalyzingTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class Producer implements Runnable {

    private final BlockingQueue<FileAnalyzingTask> queue;
    private final BlockingQueue<Record> recorderQueue;
    private final HashSet<String> fileChecked;

    public Producer(
            BlockingQueue<FileAnalyzingTask> queue,
            BlockingQueue<Record> recorderQueue
    ) {
        this.queue = queue;
        this.recorderQueue = recorderQueue;
        this.fileChecked = new HashSet<>();
        // TODO use hashmap to check which path we already checked
    }

    @Override
    public void run() {
        System.out.println("Started to scan directory...");
        try (Stream<Path> paths = Files.walk(Path.of("./data"), 1)) {
            paths.filter(Files::isRegularFile)
                .filter(this::checkIfPathWasScanned)
                .forEach(path -> {
                    try {
                        this.queue.put(new FileAnalyzingTask(path, recorderQueue));
                        this.fileChecked.add(path.toString());
                    } catch (InterruptedException e) {
                        System.err.println("Can't create task for " + path.toString() + " Error: " + e);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Finished to scan directory...");
    }

    private boolean checkIfPathWasScanned(Path path) {
        return !this.fileChecked.contains(path.toString());
    }
}