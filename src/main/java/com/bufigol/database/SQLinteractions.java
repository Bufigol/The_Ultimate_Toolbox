package com.bufigol.database;

import com.bufigol.textToolbox.BasicTextToolBox;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class, SQLinteractions, provides utility methods for interacting with a SQL database.
 * It includes methods for inserting, updating, deleting, and searching records in a table.
 */
public class SQLinteractions {

    /**
     * Inserts a single record into the specified table.
     *
     * @param connection  the database connection
     * @param table       the name of the table
     * @param columns     an array of column names
     * @param values      an array of values corresponding to the columns
     * @return true if the record was inserted successfully, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean insertIntoTableOneRecord(Connection connection, String table, String[] columns, String[] values) throws SQLException {
        // Check if the number of columns and values are equal
        if (columns.length != values.length) {
            throw new IllegalArgumentException("The number of columns and values must be the same.");
        }

        // Build the SQL query for inserting a record
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(table).append(" (");
        for (int i = 0; i < columns.length; i++) {
            queryBuilder.append(columns[i]);
            if (i < columns.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            queryBuilder.append("?");
            if (i < values.length - 1) {
                queryBuilder.append(", ");
           
