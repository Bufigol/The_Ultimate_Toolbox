package com.bufigol.fileManagment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLogger {

    private static String LOG_FILE_PATH;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs an error message with an error code.
     *
     * @param errorCode The error code from INT_CONST_ERROR_CODES
     * @param errorMessage The error message to log
     */
    public static void logErrorWithErrorCode(int errorCode, String errorMessage) throws IOException {
        if (LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        }else {
            logMessage("ERROR CODE: " + errorCode + " - " + errorMessage);
        }

    }

    /**
     * Logs a generic error message.
     *
     * @param errorMessage The error message to log
     */
    public static void logError(String errorMessage) throws IOException {
        if (LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        }else {
            logMessage("ERROR: " + errorMessage);
        }
    }

    /**
     * Logs an information message.
     *
     * @param infoMessage The information message to log
     */
    public static void logInfo(String infoMessage) throws IOException {
        if (LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        }else{
            logMessage("INFO: " + infoMessage);
        }
    }

    /**
     * Logs a warning message.
     *
     * @param warningMessage The warning message to log
     */
    public static void logWarning(String warningMessage) throws IOException {
        if (LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        }else{
            logMessage("WARNING: " + warningMessage);
        }

    }

    // Private helper method to log a message with timestamp
    private static void logMessage(String message) {
        try (BufferedWriter writer = getLogFileWriter()) {
            String timeStamp = DATE_FORMAT.format(new Date());
            writer.append(timeStamp + " - " + message + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage()); // Fallback to console
        }
    }

    // Helper method to get a BufferedWriter for the log file
    private static BufferedWriter getLogFileWriter() throws IOException {
        if (LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        }
        File logFile = new File(LOG_FILE_PATH);
        if (!logFile.exists()) {
            logFile.getParentFile().mkdirs(); // Create parent directories if needed
            logFile.createNewFile();
        }
        return new BufferedWriter(new FileWriter(logFile, true));
    }

    public static void setLogFilePath(String logFilePath) {
        LOG_FILE_PATH = logFilePath;
    }
}
