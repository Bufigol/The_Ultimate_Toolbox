package com.Bufigol.fileManagment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class, JSON_Handler, provides methods for handling JSON files, such as converting CSV data to JSON,
 * reading values from JSON files, and extracting values for a specified key from a list of JSON objects.
 */
public class JSON_Handler {

    /**
     * Converts CSV data to JSON format and writes it to a JSON file.
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

    /**
     * Reads a name/value pair from a JSON file.
     *
     * @param jsonFilePath The path to the JSON file
     * @param name         The name of the key to retrieve the value for
     * @return The value associated with the specified name, or null if not found
     * @throws RuntimeException if an error occurs during parsing or reading
     */
    public static String readValueFromJson(String jsonFilePath, String name) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(jsonFilePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            return (String) jsonObject.get(name); // Cast to String assuming values are strings
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error reading value from JSON file", e);
        }
    }

    /**
     * Reads a JSON file and returns a list of JSON objects.
     *
     * @param jsonFilePath The path to the JSON file
     * @return A list of JSONObject representing the objects in the JSON file
     * @throws RuntimeException if an error occurs during parsing or reading
     */
    public static List<JSONObject> readJsonObjects(String jsonFilePath) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Object parsedData = parser.parse(reader);
            if (parsedData instanceof JSONArray jsonArray) {
                List<JSONObject> jsonObjects = new ArrayList<>();
                for (Object obj : jsonArray) {
                    if (obj instanceof JSONObject) {
                        jsonObjects.add((JSONObject) obj);
                    } else {
                        throw new RuntimeException("Unexpected data type in JSONArray: " + obj.getClass().getName());
                    }
                }
                return jsonObjects;
            } else {
                throw new RuntimeException("JSON file does not contain a JSONArray");
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    /**
     * Retrieves a list of values for a specified key from a list of JSON objects.
     *
     * @param jsonObjects The list of JSONObject to extract values from
     * @param key         The name of the key for which to retrieve values
     * @return A list of values associated with the specified key
     */
    public static List<String> getValuesForKey(List<JSONObject> jsonObjects, String key) {
        List<String> values = new ArrayList<>();
        for (JSONObject jsonObject : jsonObjects) {
            String value = (String) jsonObject.get(key);
            values.add(value);
        }
        return values;
    }
}
