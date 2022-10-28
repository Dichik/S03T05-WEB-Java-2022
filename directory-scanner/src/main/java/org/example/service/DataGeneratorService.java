package org.example.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGeneratorService {

    private static final Random random = new Random();

    public void generate(String path) {

        int numberOfSamples = 10;
        for (int i = 0; i < numberOfSamples; ++ i) {
            String fileName = path + "/input" + i + ".txt";
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                BufferedWriter writer = new BufferedWriter(fileWriter);

                String randomData = formSampleFileData();
                writer.write(randomData);
                writer.close();
                System.out.println("File with " + fileName + " path was successfully created.");
            } catch (IOException e) {
                System.out.println("Unexpected error, please see: " + e);
            }
        }

    }

    private String formSampleFileData() {
        int randomNumber = random.nextInt() % 10;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("012345566").append(" ").append("KPetrovich");

        return stringBuilder.toString();
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

    private void cleanAllFiles(String path) {
        // TODO clear all already existed files
        // actually it workds without cleaning, but it is interesting how to clean directory
    }

}