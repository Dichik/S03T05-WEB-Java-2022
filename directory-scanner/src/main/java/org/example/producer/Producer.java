package org.example.producer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Record;
import org.example.service.FileAnalyzingTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class Producer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Producer.class);
    private static final String DEFAULT_DIRECTORY_NAME = "./data";

    private final BlockingQueue<FileAnalyzingTask> queue;
    private final BlockingQueue<Record> recorderQueue;
    private final HashSet<String> fileChecked; // TODO check by time created/updated to check file again

    public Producer(
            BlockingQueue<FileAnalyzingTask> queue,
            BlockingQueue<Record> recorderQueue
    ) {
        this.queue = queue;
        this.recorderQueue = recorderQueue;
        this.fileChecked = new HashSet<>();
    }

    @Override
    public void run() {
        logger.info("Started to scan directory + " + DEFAULT_DIRECTORY_NAME);

        try (Stream<Path> paths = Files.walk(Path.of(DEFAULT_DIRECTORY_NAME), 1)) {
            paths.filter(Files::isRegularFile)
                .filter(this::checkIfPathWasScanned)
                .forEach(path -> {
                    try {
                        this.queue.put(new FileAnalyzingTask(path, recorderQueue));
                        this.fileChecked.add(path.toString());

                        logger.info("Task for " + path + " was successfully created.");
                    } catch (InterruptedException e) {
                        logger.warn(String.format("Can't create task for %s file, error message: %s", path, e));
                    }
                });
        } catch (IOException e) {
            logger.error(String.format("Error while scanning directory %s, please see: %s", DEFAULT_DIRECTORY_NAME, e));
        }
        logger.info("Finished to scan directory " + DEFAULT_DIRECTORY_NAME);
    }

    private boolean checkIfPathWasScanned(Path path) {
        return !this.fileChecked.contains(path.toString());
    }
    
}