package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGeneratorService {

    private static final Logger logger = LogManager.getLogger(DataGeneratorService.class);

    private static final Random random = new Random();

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
        int randomNumber = random.nextInt() % 10;

        return "012345566" + " " + "KPetrovich";
        //stringBuilder.append("Sample data without required letters");
        //while (randomNumber --> 0) {
        //    if (randomNumber % 2 == 0) {
        //
        //    } else {
        //    }
        //    stringBuilder.append("\n");
        //}
        //
        //return stringBuilder.toString();
    }

}