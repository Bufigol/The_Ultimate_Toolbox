package com.bufigol.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLinteractions {

    /**
     * Inserts multiple rows of data into the specified table.
     *
     * @param table    The name of the table to insert data into.
     * @param columns An array of column names.
     * @param values  A 2D array of values, where each sub-array represents a row and
     *                contains values corresponding to the columns.
     * @return true if all insertions were successful, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public static boolean insertIntoTable(Connection connection, String table, String[] columns, String[][] values) throws SQLException {
        // Assuming you have a Connection object named 'connection' (replace with your actual connection)
        StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");
        for (String column : columns) {
            sql.append(column).append(", ");
        }
        sql.setLength(sql.length() - 2); // Remove the last comma and space
        sql.append(") VALUES (");
        for (int i = 0; i < columns.length; i++) {
            sql.append("?, ");
        }
        sql.setLength(sql.length() - 2); // Remove the last comma and space
        sql.append(")");

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (String[] row : values) {
                for (int i = 0; i < columns.length; i++) {
                    statement.setString(i + 1, row[i]);
                }
                statement.addBatch(); // Add each row to the batch
            }
            int[] rowsAffected = statement.executeBatch(); // Execute the batch insert
            for (int count : rowsAffected) {
                if (count == 0) {
                    return false; // If any row failed to insert, return false
                }
            }
            return true; // All rows inserted successfully
        }
    }
}
