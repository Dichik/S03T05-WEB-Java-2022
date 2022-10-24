package org.example.service;

import org.example.entity.Record;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class Producer implements Runnable {

    private BlockingQueue queue;
    private ExecutorService executorService;
    private FileAnalyzerService fileAnalyzerService;

    public Producer(BlockingQueue<Record> queue, ExecutorService executorService) {
        this.queue = queue;
        this.executorService = executorService;
        this.fileAnalyzerService = new FileAnalyzerService();
    }

    @Override
    public void run() {
        //for (int i = 0; i < 10; ++ i) {
        //    Message msg = new Message("message" + i);
        //    try {
        //        Thread.sleep(i);
        //        queue.put(msg);
        //        System.out.println("Produced " + msg.getRecord());
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //}

        try (Stream<Path> paths = Files.walk(Path.of("./data"), 1)) {
            paths.filter(Files::isRegularFile)
                .forEach(path -> {
                    Runnable runnable = () -> {
                        List<String> abonents = this.fileAnalyzerService.fileSatisfiesCondition(path);
                        Record record = new Record(abonents, path.toString());
                        if (!record.isEmpty()) {
                            try {
                                queue.put(record);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
                    executorService.execute(runnable);
                    System.out.println("Task for " + path.toString() + " was created.");
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}