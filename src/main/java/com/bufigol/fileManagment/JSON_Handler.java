package com.bufigol.fileManagment;

import com.bufigol.fileManagment.UniversalCSVReaderAndWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSON_Handler {
    /**
     * Reads data from a CSV file, converts it into a JSON structure, and writes it to a JSON file.
     *
     * @param csvFilePath The path to the input CSV file
     * @param jsonFilePath The path to the output JSON file
     * @throws RuntimeException if an error occurs during reading, processing, or writing
     */
    public static void csvToJson(String csvFilePath, String jsonFilePath) {
        // 1. Read CSV data
        ArrayList<String[]> csvData = UniversalCSVReaderAndWriter.readCSV(csvFilePath);

        // 2. Process data and create JSON structure
        JSONArray jsonArray = convertToJSONArray(csvData);

        // 3. Write JSON to file
        writeJsonToFile(jsonArray, jsonFilePath);
    }

    /**
     * Converts the CSV data (list of String arrays) into a JSONArray.
     *
     * @param csvData The CSV data as a list of String arrays (rows)
     * @return A JSONArray representing the converted data
     */
    private static JSONArray convertToJSONArray(ArrayList<String[]> csvData) {
        JSONArray jsonArray = new JSONArray();
        // Assuming the first row contains headers
        String[] headers = csvData.get(0);

        // Iterate through rows (excluding header)
        for (int i = 1; i < csvData.size(); i++) {
            String[] row = csvData.get(i);
            JSONObject jsonObject = new JSONObject();
            // Create key-value pairs based on headers and row values
            for (int j = 0; j < headers.length; j++) {
                jsonObject.put(headers[j], row[j]);
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * Writes the given JSONArray to a JSON file.
     *
     * @param jsonArray The JSONArray to write
     * @param jsonFilePath The path to the output JSON file
     * @throws RuntimeException if an error occurs while writing the file
     */
    private static void writeJsonToFile(JSONArray jsonArray, String jsonFilePath) {
        try (FileWriter fileWriter = new FileWriter(jsonFilePath)) {
            fileWriter.write(jsonArray.toJSONString());
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON file", e);
        }
    }
}
