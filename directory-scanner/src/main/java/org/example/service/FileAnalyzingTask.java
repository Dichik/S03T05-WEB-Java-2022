package org.example.service;

import org.example.entity.Record;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class FileAnalyzingTask implements Runnable {

    private Path path;
    private final BlockingQueue<Record> recorderQueue;

    public FileAnalyzingTask(Path path, BlockingQueue<Record> recorderQueue) {
        this.path = path;
        this.recorderQueue = recorderQueue;
    }

    @Override
    public void run() {
        File file = new File(path.toString());
        try (final FileReader fileReader = new FileReader(file)) {
            List<String> abonents = new ArrayList<>();

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!isValidAbonent(line)) {
                    continue;
                }
                abonents.add(line);
            }
            Record record = new Record(abonents, path.toString());
            this.recorderQueue.put(record);
            System.out.println(record.getMessages().toString() + " record is created.");
        } catch (Exception e) {
            System.out.println("Error occured, please see: " + e);
        }
    }

    private boolean isValidAbonent(String abonent) {
        //if (abonent == null || abonent.equals("")) {
        //    return false;
        //}
        System.out.println("Checking " + abonent);
        String[] data = abonent.split(" ");
        return data.length > 1 && (data[1].charAt(0) == 'K' || data[1].charAt(0) == 'C');
    }
}