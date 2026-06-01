package utils;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import com.opencsv.CSVReaderHeaderAware;

import config.ConfigReader;

public class CSVReader {
    // Reads login data from CSV and returns a map for the specified type
    public static Map<String, String> getRowByType(String type) {

        String csvPath = ConfigReader.get("loginDataPath");
        Map<String, String> row = new HashMap<>();

        try {
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(csvPath));
            Map<String, String> line;

            while ((line = reader.readMap()) != null) {
                if (line.get("type").equals(type)) {
                    row = new HashMap<>(line);
                    break;
                }
            }
            reader.close();

        } catch (Exception e) {
            System.out.println("CSV read error: " + e.getMessage());
        }
        return row;
    }

    // Reads notes data from CSV, appends timestamp to noteTitle, and returns a map
    public static Map<String, String> getNotesRowByType(String type) {

        String csvPath = ConfigReader.get("notesDataPath");
        Map<String, String> row = new HashMap<>();

        try {
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(csvPath));
            Map<String, String> line;

            while ((line = reader.readMap()) != null) {
                if (line.get("type").equals(type)) {
                    row = new HashMap<>(line);
                    break;
                }
            }
            reader.close();

        } catch (Exception e) {
            System.out.println("CSV read error: " + e.getMessage());
        }

        if (row.containsKey("noteTitle")
                && row.get("noteTitle") != null
                && !row.get("noteTitle").trim().isEmpty()) {
            String uniqueTitle = row.get("noteTitle") + " " + System.currentTimeMillis();
            row.put("noteTitle", uniqueTitle);
        }
        return row;
    }

}