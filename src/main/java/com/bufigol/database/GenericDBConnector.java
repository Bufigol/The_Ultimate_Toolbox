package com.bufigol.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * GenericDBConnector is a Singleton class that establishes a connection to a database using the provided configuration parameters.
 */
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
     * @param usr  The database username
     * @param pwd  The database password
     */
    // The constructor initializes the connection using the provided configuration parameters
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

    // Default constructor that initializes the connection with default configuration parameters
    public GenericDBConnector() {
        this("jdbc:mysql://localhost:3306/test", "root", "root");
    }

    /**
     * Closes the database connection.
     */
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage()); // Replace with your logging mechanism
        }
    }

    /**
     * Returns the Singleton instance of the GenericDBConnector class.
     *
     * @return The Singleton instance of the GenericDBConnector class.
     */
    public static GenericDBConnector getInstance() {
        if (instance == null) {
            instance = new GenericDBConnector();
        }
        return instance;
    }

    /**
     * Returns the Connection object associated with this GenericDBConnector instance.
     *
     * @return The Connection object associated with this GenericDBConnector instance.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Sets the Connection object associated with this GenericDBConnector instance.
     *
     * @param connection The Connection object to associate with this GenericDBConnector instance.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Returns the database connection URL.
     *
     * @return The database connection URL.
     */
    public String getConnectionUrl() {
        return connectionUrl;
   
