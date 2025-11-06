package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<String[]> readCSV(String filePath) {
        List<String> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                values.add(data[0]); // Adjust index based on your CSV structure
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return List.of();
    }
}




