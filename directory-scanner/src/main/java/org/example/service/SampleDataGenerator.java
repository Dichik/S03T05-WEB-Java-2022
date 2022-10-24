package org.example.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SampleDataGenerator {

    private static final Random random = new Random();

    public void generate(String path) {
        // first of all clean data in the path
        int numberOfSamples = 10;
        for (int i = 0; i < numberOfSamples; ++ i) {
            File file = new File("input" + i + ".txt");
            try (FileWriter fileWriter = new FileWriter(file)) {
                String sampleString = formSampleFileData();
                fileWriter.write(sampleString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private String formSampleFileData() {
        int randomNumber = random.nextInt() % 10;
        StringBuilder stringBuilder = new StringBuilder();

        while (randomNumber --> 0) {
            if (randomNumber % 2 == 0) {
                stringBuilder.append("Sample data without required letters");
            } else {
                stringBuilder.append("012345566").append(" ").append(" KPetrovich");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

}