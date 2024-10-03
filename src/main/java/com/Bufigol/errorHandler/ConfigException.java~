package com.Bufigol.errorHandler;

/**
 * ConfigException is a custom exception class that extends the Java Exception class.
 * It is used to indicate that there has been an error related to configuration.
 *
 * @author Your Name
 */
public class ConfigException extends Exception {
    /**
     * Constructs a new ConfigException with the specified detail message.
     *
     * @param message The detail message for this exception.
     */
    public ConfigException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConfigException with the specified detail message and cause.
     *
     * @param message The detail message for this exception.
     * @param cause   The cause of this exception.
     */
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ConfigException with the specified detail message, cause, and error code.
     *
     * @param message The detail message for this exception.
     * @param cause   The cause of this exception.
     * @param errorCode The error code associated with this exception.
     */
    public ConfigException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * The error code associated with this exception.
     */
    private int errorCode;

    /**
     * Returns the error code associated with this exception.
     *
     * @return The error code associated with this exception.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code associated with this exception.
     *
     * @param errorCode The error code to set.
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

