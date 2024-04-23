package com.bufigol.fileManagment;

import java.io.*;
import java.util.ArrayList;

public class UniversalCSVReaderAndWriter {

    /**
     * Reads a CSV file and returns a list of records.
     *
     * @param filePath The path to the CSV file
     * @return A list of records, where each record is a String array representing a row in the CSV file
     * @throws RuntimeException if an error occurs while reading the file
     */
    public static ArrayList<String[]> readCSV(String filePath) {
        ArrayList<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                records.add(values);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
        return records;
    }

    /**
     * Appends a new record to a CSV file.
     *
     * @param filePath The path to the CSV file
     * @param record The record to append as a String array
     * @throws RuntimeException if an error occurs while writing to the file
     */
    public static void appendRecordToCSV(String filePath, String[] record) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(String.join(";", record));
        } catch (IOException e) {
            throw new RuntimeException("Error writing to CSV file", e);
        }
    }
    /**
     * Creates a new CSV file with the specified file name and optional header row.
     *
     * @param fileName The name of the CSV file to create
     * @param headerRow (Optional) A String array representing the header row to include in the CSV file. Pass null if no header is needed.
     * @throws RuntimeException if an error occurs while creating the file
     */
    public static void createNewCSVFile(String fileName, String[] headerRow) {
        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                // File created successfully
                if (headerRow != null) {
                    // Append header row if provided
                    appendRecordToCSV(fileName, headerRow);
                }
            } else {
                throw new RuntimeException("File already exists: " + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating CSV file: " + fileName, e);
        }
    }
}

