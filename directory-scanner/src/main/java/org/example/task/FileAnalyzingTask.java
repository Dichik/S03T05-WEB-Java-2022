package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Record;
import org.example.task.TaskDescription;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class FileAnalyzingTask implements Runnable, TaskDescription {
    private static final Logger logger = LogManager.getLogger(FileAnalyzingTask.class);

    private Path path;
    private final BlockingQueue<Record> recorderQueue;

    public FileAnalyzingTask(Path path, BlockingQueue<Record> recorderQueue) {
        this.path = path;
        this.recorderQueue = recorderQueue;
    }

    @Override
    public void run() {
        File file = new File(path.toString());
        try (FileReader fileReader = new FileReader(file)) {
            List<String> abonents = new ArrayList<>();

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!isValidAbonent(line)) {
                    continue;
                }
                abonents.add(line);
            }

            if (abonents.isEmpty()) {
                logger.info("Didn't create any record for " + path);
                return;
            }

            Record record = new Record(abonents, path.toString());
            this.recorderQueue.put(record);

            logger.info(record.getMessages().toString() + " record was created.");
        } catch (Exception e) {
            logger.warn("Error occured while processing analyzing task, please see: " + e);
        }
    }

    private boolean isValidAbonent(String abonent) {
        String[] data = abonent.split(" ");
        return data.length > 1 && (data[1].charAt(0) == 'K' || data[1].charAt(0) == 'C');
    }

    @Override
    public String getDescription() {
        return "Analazying task for " + path;
    }
}