package org.example.service;


import org.example.entity.Record;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private final BlockingQueue<FileAnalyzingTask> queue;

    public Consumer(BlockingQueue<FileAnalyzingTask> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        boolean isInterrupted = false;
        while ( !isInterrupted ) {
            try {
                FileAnalyzingTask task = this.queue.take();
                task.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
                isInterrupted = true;
            }
        }
    }

}