package org.example.service;

import org.example.entity.Record;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileAnalyzerService implements IFileAnalyzer<Record> {

    @Override
    public Optional<Record> analyze(Path path) {
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
            return Optional.of(record);
        } catch (Exception e) {
            System.out.println("Error occured, please see: " + e);
        }
        return Optional.empty();
    }

    private boolean isValidAbonent(String abonent) {
        System.out.println("Checking " + abonent);
        String[] data = abonent.split(" ");
        return data.length > 1 && (data[1].charAt(0) == 'K' || data[1].charAt(0) == 'C');
    }

}