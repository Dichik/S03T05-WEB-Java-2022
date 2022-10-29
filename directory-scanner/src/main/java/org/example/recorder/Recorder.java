package org.example.recorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Record;
import org.example.exception.UnableToReadFileDataError;
import org.example.exception.UnableToWriteToFileError;

import java.io.*;
import java.util.concurrent.BlockingQueue;

public class Recorder implements Runnable {
    private static final Logger logger = LogManager.getLogger(Recorder.class);
    private static final String DEFAULT_REPORT_PATH = "./report/report.txt";

    private final BlockingQueue<Record> recorderQueue;

    public Recorder(BlockingQueue<Record> recorderQueue) {
        this.recorderQueue = recorderQueue;
    }

    @Override
    public void run() {
        boolean isInterrupted = false;
        while (!isInterrupted) {
            try {
                Record record = this.recorderQueue.take();
                String reportRecord = this.formReportRecord(record);
                this.record(reportRecord);
            } catch (InterruptedException e) {
                isInterrupted = true;
                logger.warn("Interrupted error occured, please see: " + e);
            } catch (UnableToReadFileDataError e) {
                logger.error("Unable to read data from the file. Please check details: " + e); // TODO params for exceptions -> logger here, formed error messages higher
            } catch (UnableToWriteToFileError e) {
                logger.error(String.format("Couldn't write %s to %s, error message: %s", e.getRecord(), DEFAULT_REPORT_PATH, e));
            }
        }
    }

    private String formReportRecord(Record record) throws UnableToReadFileDataError {
        String line;
        String message = record.getMessages().toString() + " from the file: " + record.getPath() + "\n";
        StringBuilder sb = new StringBuilder();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(DEFAULT_REPORT_PATH))) {
            while ((line = fileReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            sb.append(message);
            logger.info(String.format("%s report record for %s was successfully formed.", DEFAULT_REPORT_PATH, message));
        } catch (IOException e) {
            throw new UnableToReadFileDataError(UnableToReadFileDataError.DEFAULT_ERROR_MESSAGE, DEFAULT_REPORT_PATH);
        }
        return sb.toString();
    }

    private void record(String reportRecord) throws UnableToWriteToFileError {
        PrintWriter writer;
        try (FileWriter fileWriter = new FileWriter(DEFAULT_REPORT_PATH)) {
            writer = new PrintWriter(fileWriter, true);
            writer.append(reportRecord);
            writer.close();
            logger.info(String.format("%s report was successfully created in %s.", reportRecord, DEFAULT_REPORT_PATH));
        } catch (IOException e) {
            throw new UnableToWriteToFileError(e.toString(), reportRecord);
        }
    }

}