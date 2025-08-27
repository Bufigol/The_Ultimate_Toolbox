package com.the_ultimate_toolbox.files.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ErrorLogger is a class that logs error messages, information messages, warning messages, and error codes to a text file.
 * It creates a new log file if it doesn't exist, and appends messages to the end of the file.
 */
public class ErrorLogger {

    private String logFilePath;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = LogManager.getLogger(ErrorLogger.class);

    /**
     * Constructs an ErrorLogger object with a specified log file path.
     *
     * @param path The path of the log file.
     */
    public ErrorLogger(String path) {
        this.logFilePath = path;
    }

    /**
     * Logs an error message with an error code to the log file.
     *
     * @param errorCode    The error code from INT_CONST_ERROR_CODES.
     * @param errorMessage The error message to log.
     * @throws IOException If there is an error writing to the log file.
     */
    public void logErrorWithErrorCode(int errorCode, String errorMessage) throws IOException {
        if (this.logFilePath == null) {
            throw new IOException("Log file path not set");
        }
        logMessage("ERROR CODE: " + errorCode + " - " + errorMessage);
    }

    /**
     * Logs a generic error message to the log file.
     *
     * @param errorMessage The error message to log.
     * @throws IOException If there is an error writing to the log file.
     */
    public void logError(String errorMessage) throws IOException {
        if (this.logFilePath == null) {
            throw new IOException("Log file path not set");
        }
        logMessage("ERROR: " + errorMessage);
    }

    /**
     * Logs an information message to the log file.
     *
     * @param infoMessage The information message to log.
     * @throws IOException If there is an error writing to the log file.
     */
    public void logInfo(String infoMessage) throws IOException {
        if (this.logFilePath == null) {
            throw new IOException("Log file path not set");
        }
        logMessage("INFO: " + infoMessage);
    }

    /**
     * Logs a warning message to the log file.
     *
     * @param warningMessage The warning message to log.
     * @throws IOException If there is an error writing to the log file.
     */
    public void logWarning(String warningMessage) throws IOException {
        if (this.logFilePath == null) {
            throw new IOException("Log file path not set");
        }
        logMessage("WARNING: " + warningMessage);

    }

    /**
     * Logs a message with a timestamp to the log file.
     *
     * @param message The message to log.
     */
    private void logMessage(String message) {
        try (BufferedWriter writer = getLogFileWriter()) {
            String timeStamp = DATE_FORMAT.format(new Date());
            writer.append(timeStamp).append(" - ").append(message).append("\n");
        } catch (IOException e) {
            logger.error("Error writing to log file: {}", e.getMessage());
        }
    }

    /**
     * Returns a BufferedWriter for the log file.
     *
     * @return A BufferedWriter for the log file.
     * @throws IOException If there is an error getting the BufferedWriter.
     */
    private BufferedWriter getLogFileWriter() throws IOException {
        if (this.logFilePath == null) {
            throw new IOException("Log file path not set");
        }
        File logFile = new File(logFilePath);
        if (!logFile.exists()) {
            logFile.getParentFile().mkdirs(); // Create parent directories if needed
            logFile.createNewFile();
        }
        return new BufferedWriter(new FileWriter(logFile, true));
    }

    /**
     * Sets the log file path.
     *
     * @param logFilePath The new log file path.
     */
    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }
}
