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

        // TODO Logging system to add

        // TODO if we finish all activities with the directory -> wait for finishing all threads and print result

        new DataGeneratorService().generate("./data");

        BlockingQueue<Record> recordingQueue = new SynchronousQueue<>();
        BlockingQueue<FileAnalyzingTask> queue = new LinkedBlockingQueue<>();

        ExecutorService consumerExecutorService = Executors.newFixedThreadPool(2);
        consumerExecutorService.submit(new Consumer(queue));
        consumerExecutorService.submit(new Consumer(queue));

        ExecutorService recordingExecutorService = Executors.newSingleThreadExecutor();
        recordingExecutorService.submit(new Recorder(recordingQueue));

        ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
        Runnable directoryScanningTask = new Producer(queue, recordingQueue);
        threadPool.scheduleAtFixedRate(directoryScanningTask, 0, 30, TimeUnit.SECONDS);

        consumerExecutorService.shutdown();
        recordingExecutorService.shutdown();

    }

}