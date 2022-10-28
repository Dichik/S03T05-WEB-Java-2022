package org.example.consumer;


import org.example.service.FileAnalyzingTask;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private final BlockingQueue<FileAnalyzingTask> fileAnazingQueue;

    public Consumer(
            BlockingQueue<FileAnalyzingTask> fileAnazingQueue
    ) {
        this.fileAnazingQueue = fileAnazingQueue;
    }

    @Override
    public void run() {
        boolean isInterrupted = false;
        while ( !isInterrupted ) {
            try {
                FileAnalyzingTask task = this.fileAnazingQueue.take();
                task.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
                isInterrupted = true;
            }
        }
    }

}