package org.example.service;


import org.example.entity.Record;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private BlockingQueue<Record> queue;

    public Consumer(BlockingQueue<Record> queue) {
        this.queue = queue;
    }


    @Override
    public void run() {

        try {
            Record msq;
            while ( !(msq = queue.take()).isEmpty() ) {
                Thread.sleep(10);
                System.out.println("Do something with the job, path = " + msq.getPath());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}