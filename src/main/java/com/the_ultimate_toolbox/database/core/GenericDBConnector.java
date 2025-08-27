package com.the_ultimate_toolbox.database.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GenericDBConnector {

    private static volatile GenericDBConnector instance;
    private Connection connection;
    private String connectionUrl;
    private String username;
    private String password;
    private static final Logger logger = LogManager.getLogger(GenericDBConnector.class);


    private GenericDBConnector(String curl, String usr, String pwd) {
        String DRIVER_JDBC = "com.mysql.cj.jdbc.Driver";
        if (!(curl.isBlank() || curl.isEmpty())) {
            this.connectionUrl = curl;
            if (!(usr.isBlank() || usr.isEmpty())) {
                this.username = usr;
                if (!(pwd.isBlank() || pwd.isEmpty())) {
                    this.password = pwd;
                    try {
                        // Database configuration parameters (make these configurable)
                        Class.forName(DRIVER_JDBC);
                        connection = DriverManager.getConnection(connectionUrl, username, password);
                        logger.info("Database connection established.");
                    } catch (ClassNotFoundException | SQLException e) {
                        logger.error("Error establishing database connection: " + e.getMessage());
                    }
                }
            }
        }
    }

    private GenericDBConnector() {
        // private constructor to prevent instantiation
    }

    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection: " + e.getMessage());
        }
    }

    public static GenericDBConnector getInstance(String curl, String usr, String pwd) {
        if (instance == null) {
            synchronized (GenericDBConnector.class) {
                if (instance == null) {
                    instance = new GenericDBConnector(curl, usr, pwd);
                }
            }
        }
        return instance;
    }

    public static GenericDBConnector getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GenericDBConnector is not initialized. Please call getInstance(curl, usr, pwd) first.");
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}