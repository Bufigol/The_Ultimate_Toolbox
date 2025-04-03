package com.bufigol.fileManagment;

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

    private String LOG_FILE_PATH;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructs an ErrorLogger object with a specified log file path.
     * If the path is not null, it creates a new log file at that location.
     *
     * @param path The path of the log file.
     */
    public ErrorLogger(String path) {
        this.LOG_FILE_PATH = path;
        if (path != null) {
            this.LOG_FILE_PATH = crearArchivoTxt("log");
        }
        initializeLogFile(path);
    }

    /**
     * Initializes the log file by creating it if it doesn't exist.
     *
     * @param path The path of the log file.
     */
    private static void initializeLogFile(String path) {
        File logFile = new File(path);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Logs an error message with an error code to the log file.
     *
     * @param errorCode The error code from INT_CONST_ERROR_CODES.
     * @param errorMessage The error message to log.
     * @throws IOException If there is an error writing to the log file.
     */
    public void logErrorWithErrorCode(int errorCode, String errorMessage) throws IOException {
        if (this.LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        } else {
            logMessage("ERROR CODE: " + errorCode + " - " + errorMessage);
        }

    }

    /**
     * Logs a generic error message to the log file.
     *
     * @param errorMessage The error message to log.
     * @throws IOException If there is an error writing to the log file.
     */
    public void logError(String errorMessage) throws IOException {
        if (this.LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        } else {
            logMessage("ERROR: " + errorMessage);
        }
    }

    /**
     * Logs an information message to the log file.
     *
     * @param infoMessage The information message to log.
     * @throws IOException If there is an error writing to the log file.
     */
    public void logInfo(String infoMessage) throws IOException {
        if (this.LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        } else {
            logMessage("INFO: " + infoMessage);
        }
    }

    /**
     * Logs a warning message to the log file.
     *
     * @param warningMessage The warning message to log.
     * @throws IOException If there is an error writing to the log file.
     */
    public void logWarning(String warningMessage) throws IOException {
        if (this.LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        } else {
            logMessage("WARNING: " + warningMessage);
        }

    }

    /**
     * Logs a message with a timestamp to the log file.
     *
     * @param message The message to log.
     */
    private void logMessage(String message) {
        try (BufferedWriter writer = getLogFileWriter()) {
            String timeStamp = DATE_FORMAT.format(new Date());
            writer.append(timeStamp + " - " + message + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage()); // Fallback to console
        }
    }

    /**
     * Returns a BufferedWriter for the log file.
     *
     * @return A BufferedWriter for the log file.
     * @throws IOException If there is an error getting the BufferedWriter.
     */
    private BufferedWriter getLogFileWriter() throws IOException {
        if (this.LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        }
        File logFile = new File(LOG_FILE_PATH);
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
        this.LOG_FILE_PATH = logFilePath;
    }

    /**
     * Creates a new text file with a specified name.
     *
     * @param name The name of the text file.
     * @return The path of the new text file.
     */
    public String crearArchivoTxt(String name) {
        // Obtener la ruta del directorio donde se está ejecutando el programa
        String directorioActual = System.getProperty("user.dir");

        // Construir la ruta completa del archivo de texto
        String rutaArchivo = directorioActual + "/" + name + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            // Escribir la ruta en el archivo
            writer.write(rutaArchivo);
            return rutaArchivo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
