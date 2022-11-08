package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class DataGeneratorService {

    private static final Logger logger = LogManager.getLogger(DataGeneratorService.class);

    public void generate(String path) {
        logger.info("Started to generate sample data.");

        int numberOfSamples = 10;
        for (int i = 0; i < numberOfSamples; ++i) {
            String fileName = path + "/input" + i + ".txt";
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                BufferedWriter writer = new BufferedWriter(fileWriter);
                String randomData = formSampleFileData();
                writer.write(randomData);
                writer.close();
            } catch (IOException e) {
                logger.warn("Error while generating sample data, see: " + e);
            }
        }
        logger.info("Finish to generate sample data.");
    }

    private String formSampleFileData() {
        return LocalTime.now() + " " + "KPetrovich";
    }

}