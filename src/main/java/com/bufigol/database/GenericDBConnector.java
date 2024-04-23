package com.bufigol.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GenericDBConnector {

    private static GenericDBConnector instance;
    private Connection connection;

    // Database configuration parameters (make these configurable)
    private String driverClass;
    private String connectionUrl;
    private String username;
    private String password;

    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the connection using the provided configuration parameters.
     *
     * @param driverClass   The JDBC driver class name
     * @param connectionUrl The database connection URL
     * @param username      The database username
     * @param password      The database password
     */
    private GenericDBConnector(String driverClass, String connectionUrl, String username, String password) {
        this.driverClass = driverClass;
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;

        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(connectionUrl, username, password);
            System.out.println("Database connection established."); // Replace with your logging mechanism
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error establishing database connection: " + e.getMessage()); // Replace with your logging mechanism
        }
    }

    /**
     * Gets the singleton instance of the GenericDBConnector.
     *
     * @param driverClass   The JDBC driver class name
     * @param connectionUrl The database connection URL
     * @param username      The database username
     * @param password      The database password
     * @return The singleton instance of GenericDBConnector
     */
    public static GenericDBConnector getInstance(String driverClass, String connectionUrl, String username, String password) {
        if (instance == null) {
            instance = new GenericDBConnector(driverClass, connectionUrl, username, password);
        }
        return instance;
    }

    /**
     * Gets the established database connection.
     *
     * @return The database connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed."); // Replace with your logging mechanism
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage()); // Replace with your logging mechanism
        }
    }
}
