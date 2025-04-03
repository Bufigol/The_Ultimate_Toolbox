package com.ultimatetoolbox.database.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GenericDBConnector {

    private static GenericDBConnector instance;
    private Connection connection;
    private String connectionUrl;
    private String username;
    private String password;

    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the connection using the provided configuration parameters.
     *
     * @param curl The database connection URL
     * @param usr      The database username
     * @param pwd      The database password
     */
    public GenericDBConnector(String curl, String usr, String pwd) {
        String DRIVER_JDBC = "com.mysql.cj.jdbc.Driver";
        boolean conection =false;
        if (!(curl.isBlank() || curl.isEmpty())){
            this.connectionUrl = curl;
            if (!(usr.isBlank() || usr.isEmpty())){
                this.username = usr;
                if (!(pwd.isBlank() || pwd.isEmpty())){
                    this.password = pwd;
                    
                    conection=true;
                }
            }
        }
        if (conection){
            try {
                // Database configuration parameters (make these configurable)
                Class.forName(DRIVER_JDBC);
                connection = DriverManager.getConnection(connectionUrl, username, password);
                System.out.println("Database connection established."); // Replace with your logging mechanism
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Error establishing database connection: " + e.getMessage()); // Replace with your logging mechanism
            }
        }
    }
    public GenericDBConnector() {
        this("jdbc:mysql://localhost:3306/test", "root", "root");
    }
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage()); // Replace with your logging mechanism
        }
    }
    public static GenericDBConnector getInstance() {
        if (instance == null) {
            instance = new GenericDBConnector();
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