package org.example.consumer;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.FileAnalyzingTask;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private static final Logger logger = LogManager.getLogger(Consumer.class);

    private final BlockingQueue<FileAnalyzingTask> fileAnazingQueue;

    public Consumer(
            BlockingQueue<FileAnalyzingTask> fileAnazingQueue
    ) {
        this.fileAnazingQueue = fileAnazingQueue;
    }

    @Override
    public void run() {
        boolean isInterrupted = false;
        while (!isInterrupted) {
            try {
                FileAnalyzingTask task = this.fileAnazingQueue.take();
                task.run();
                logger.info("Executing task for " + task.getDescription());
            } catch (InterruptedException e) {
                isInterrupted = true;
                logger.error("Consumer was interrpted, see: " + e);
            }
        }
    }

}