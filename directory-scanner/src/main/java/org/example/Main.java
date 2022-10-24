package org.example;

import org.example.service.Consumer;
import org.example.service.Producer;
import org.example.entity.Record;

import java.io.File;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        BlockingQueue<Record> queue = new ArrayBlockingQueue<>(10);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Producer producer = new Producer(queue, executorService);
        Consumer consumer = new Consumer(queue);

        new Thread(producer).start();
        new Thread(consumer).start();

        System.out.println("Producer and Consumer has been started");

        //System.out.println("Please enter directory name: ");
        //
        //Scanner scanner = new Scanner(System.in);
        //String path = scanner.nextLine();
        //
        //File file = new File(path);
        //if (!file.isDirectory()) {
        //    throw new UnexistedDirectoryError();
        //} else {
        //    new SampleDataGenerator().generate(file.getPath());
        //}
        //
        //
        //ExecutorService executorService = null;
        //Runnable runnable = () -> {
        //    try (FileInputStream fileInputStream = new FileInputStream(path)) {
        //    } catch (IOException e) {
        //        throw new RuntimeException(e);
        //    }
        //};
        //// TODO generate files randomly
        //try {
        //    executorService = Executors.newFixedThreadPool(5);
        //    int numberOfFiles = 2;
        //    for (int i = 0; i < numberOfFiles; ++ i) {
        //        Runnable curreentRunnable = processFile(executorService, null);
        //        executorService.execute(curreentRunnable);
        //    }
        //
        //    executorService.awaitTermination(1, TimeUnit.MINUTES);
        //    if(executorService.isTerminated()) {
        //        System.out.println("Finished!");
        //    } else System.out.println("At least one task is still running");
        //} catch (Exception e) {
        //    System.out.println("Error " + e);
        //} finally {
        //    executorService.shutdown();
        //}

        // check out if directory exist or take default nextLine
        // create thread executor for reading 5 files
        // check out if file has string that satisfies regex
        // analyse if there is owner name
        // add logger
        // if we have K and C add this file name to thread queue where we are storing data to the file
        // if we finish all activities with the directory -> wait for finishing all threads and print result

    }

    private static Runnable processFile(ExecutorService executorService, File file) throws Exception {
        return () -> {
            if (!checkFileData("string")) {
                // ...
            }
            // ...
        };
    }

    private static boolean checkFileData(String string) {
        // TODO
        return true;
    }

}