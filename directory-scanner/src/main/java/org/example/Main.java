package org.example;

import org.example.consumer.Consumer;
import org.example.entity.Record;
import org.example.producer.Producer;
import org.example.recorder.Recorder;
import org.example.service.*;
import org.example.service.FileAnalyzingTask;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {

        new DataGeneratorService().generate("./data");

        BlockingQueue<Record> recordingQueue = new SynchronousQueue<>();

        BlockingQueue<FileAnalyzingTask> queue = new LinkedBlockingQueue<>();
        ExecutorService consumerExecutorService = Executors.newFixedThreadPool(2);
        ExecutorService recordingExecutorService = Executors.newSingleThreadExecutor();

        ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);

        Runnable directoryCheckerTask = new Producer(queue, recordingQueue);

        threadPool.scheduleAtFixedRate(directoryCheckerTask, 0, 30, TimeUnit.SECONDS);

        consumerExecutorService.submit(new Consumer(queue));
        consumerExecutorService.submit(new Consumer(queue));

        recordingExecutorService.submit(new Recorder(recordingQueue));

        System.out.println("Producer and Consumer has been started");

        consumerExecutorService.shutdown();
        recordingExecutorService.shutdown();

        // TODO Logging system to add

        // TODO if we finish all activities with the directory -> wait for finishing all threads and print result
    }

}