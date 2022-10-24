package org.example.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileAnalyzerService implements IFileAnalyzer {

    public List<String> fileSatisfiesCondition(Path path) {
        List<String> abonents = new ArrayList<>();

        File file = new File(path.toString());
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (foundAbonentWithCondition(line)) {
                    abonents.add(line);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return abonents;
    }

    private boolean foundAbonentWithCondition(String abonent) {
        String[] data = abonent.split(" ");
        //return data[1].charAt(0) == 'K' || data[1].charAt(0) == 'C';
        return true;
    }

}