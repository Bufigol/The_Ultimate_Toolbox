package com.bufigol.fileManagment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLogger {

    private String LOG_FILE_PATH;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ErrorLogger(String path) {
        this.LOG_FILE_PATH = path;
        if(path != null) {
            this.LOG_FILE_PATH=crearArchivoTxt("log");
        }
        initializeLogFile(path);
    }

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
     * Logs an error message with an error code.
     *
     * @param errorCode The error code from INT_CONST_ERROR_CODES
     * @param errorMessage The error message to log
     */
    public  void logErrorWithErrorCode(int errorCode, String errorMessage) throws IOException {
        if (this.LOG_FILE_PATH == null) {
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
    public void logError(String errorMessage) throws IOException {
        if (this.LOG_FILE_PATH == null) {
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
    public void logInfo(String infoMessage) throws IOException {
        if (this.LOG_FILE_PATH == null) {
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
    public void logWarning(String warningMessage) throws IOException {
        if (this.LOG_FILE_PATH == null) {
            throw new IOException("Log file path not set");
        }else{
            logMessage("WARNING: " + warningMessage);
        }

    }

    // Private helper method to log a message with timestamp
    private  void logMessage(String message) {
        try (BufferedWriter writer = getLogFileWriter()) {
            String timeStamp = DATE_FORMAT.format(new Date());
            writer.append(timeStamp + " - " + message + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage()); // Fallback to console
        }
    }

    // Helper method to get a BufferedWriter for the log file
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

    public void setLogFilePath(String logFilePath) {
        this.LOG_FILE_PATH = logFilePath;
    }

    public  String crearArchivoTxt(String name) {
        // Obtener la ruta del directorio donde se está ejecutando el programa
        String directorioActual = System.getProperty("user.dir");

        // Construir la ruta completa del archivo de texto
        String rutaArchivo = directorioActual + "/"+name+".txt";

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
