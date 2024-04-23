package com.bufigol.errorHandler;
import com.bufigol.fileManagment.ErrorLogger;

import java.io.IOException;

public class ConfigException extends Exception {
    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
    public ConfigException(String message, Throwable cause,int errorCode) {
        super(message, cause);
    }
}
