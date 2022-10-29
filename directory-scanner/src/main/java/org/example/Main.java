package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.consumer.Consumer;
import org.example.entity.Record;
import org.example.producer.Producer;
import org.example.recorder.Recorder;
import org.example.service.DataGeneratorService;
import org.example.service.FileAnalyzingTask;

import java.util.concurrent.*;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String DEFAULT_DATA_DIRECTORY = "./data";

    public static void main(String[] args) {
        logger.info("Application successfully started.");

        new DataGeneratorService().generate(DEFAULT_DATA_DIRECTORY);

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

        logger.info("Application successfully finished.");
    }

}