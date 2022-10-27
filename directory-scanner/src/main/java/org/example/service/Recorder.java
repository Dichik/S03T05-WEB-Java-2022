package org.example.service;

import org.example.entity.Record;

import java.io.*;
import java.util.concurrent.BlockingQueue;

public class Recorder implements Runnable {

    private final BlockingQueue<Record> recorderQueue;

    public Recorder(BlockingQueue<Record> recorderQueue) {
        this.recorderQueue = recorderQueue;
    }

    @Override
    public void run() {


        boolean isInterrupted = false;
        while ( !isInterrupted ) {
            try {
                Record task = this.recorderQueue.take();
                String message =  task.getMessages().toString() + " from the file: " + task.getPath() + "\n";
                System.out.println("Recording to the file... Messages: " + message);

                String path = "./report/report.txt";
                StringBuilder sb = new StringBuilder();

                BufferedReader fileReader = new BufferedReader(new FileReader(path));
                String line;
                while( (line = fileReader.readLine()) != null ) {
                    sb.append(line).append("\n");
                }
                sb.append(message);

                FileWriter fileWriter = new FileWriter(path);
                PrintWriter writer = new PrintWriter(fileWriter, true);
                writer.append(sb.toString());
                writer.close();

            } catch (InterruptedException e) {
                e.printStackTrace();
                isInterrupted = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}