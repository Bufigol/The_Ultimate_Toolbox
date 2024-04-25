package com.bufigol.database;

import com.bufigol.textToolbox.BasicTextToolBox;

import java.sql.*;

public class SQLinteractions {


    /**
     * Inserts a single record into the specified table.
     *
     * @param  connection the database connection
     * @param  table      the name of the table
     * @param  columns    an array of column names
     * @param  values     an array of values corresponding to the columns
     * @return            true if the record was inserted successfully, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean insertIntoTableOneRecord(Connection connection, String table, String[] columns, String[] values) throws SQLException {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("The number of columns and values must be the same.");
        }
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(table).append(" (");
        // Construye la lista de columnas en la consulta SQL
        for (int i = 0; i < columns.length; i++) {
            queryBuilder.append(columns[i]);
            if (i < columns.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(") VALUES (");
        // Construye la lista de valores en la consulta SQL
        for (int i = 0; i < values.length; i++) {
            queryBuilder.append("?");
            if (i < values.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");

        String query = queryBuilder.toString();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Establece los valores de los parámetros en la consulta preparada
            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, values[i]);
            }

            // Ejecuta la consulta
            int filasInsertadas = pstmt.executeUpdate();

            // Retorna true si al menos una fila fue insertada, de lo contrario retorna false
            return filasInsertadas > 0;
        }
    }
    public static boolean insertIntoTableOneRecord(Connection connection, String table, String[] columns, String[] values,String[] types) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(table).append(" (");
        // Construye la lista de columnas en la consulta SQL
        for (int i = 0; i < columns.length; i++) {
            queryBuilder.append(columns[i]);
            if (i < columns.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(") VALUES (");
        // Construye la lista de valores en la consulta SQL
        for (int i = 0; i < values.length; i++) {
            queryBuilder.append("?");
            if (i < values.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");

        String query = queryBuilder.toString();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Establece los valores de los parámetros en la consulta preparada
            for (int i = 0; i < values.length; i++) {
                String type= types[i];
                switch (type) {
                    case "INT":
                        pstmt.setInt(i + 1, Integer.parseInt(values[i]));
                        break;
                    case "FLOAT":
                        pstmt.setFloat(i + 1, Float.parseFloat(values[i]));
                        break;
                    case "DOUBLE":
                        pstmt.setDouble(i + 1, Double.parseDouble(values[i]));
                        break;
                    default:
                        pstmt.setString(i + 1, values[i]);
                        break;
                }
                pstmt.setString(i + 1, values[i]);
            }

            // Ejecuta la consulta
            int filasInsertadas = pstmt.executeUpdate();

            // Retorna true si al menos una fila fue insertada, de lo contrario retorna false
            return filasInsertadas > 0;
        }
    }


    public static int insertIntoTableMulipleRecords(Connection connection, String table, String[] columns, String[][] values) throws SQLException {
        Boolean out = true;
        int i=0;
        while (i < values.length && out) {
            out = insertIntoTableOneRecord(connection, table, columns, values[i]);
            i++;
        }
        return i;
    }

    public static int insertIntoTableMulipleRecords(Connection connection, String table, String[] columns, String[][] values,String[] types) throws SQLException {
        Boolean out = true;
        int i=0;
        while (i < values.length && out) {
            out = insertIntoTableOneRecord(connection, table, columns, values[i],types);
            i++;
        }
        return i;
    }

        /**
         * Counts the number of rows in the specified table.
         *
         * @param  connection the database connection
         * @param  table      the name of the table
         * @return            the number of rows in the table, or 0 if the table is empty
         * @throws SQLException if a database access error occurs
         */
        public static int countRows(Connection connection, String table) throws SQLException {
            String sql = "SELECT COUNT(*) FROM " + table;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    } else {
                        return 0;
                    }
                }
            }
        }
    /**
     * Deletes a row from the specified table based on the given ID.
     *
     * @param  connection the database connection
     * @param  table      the name of the table
     * @param  id         the ID of the row to be deleted
     * @return            true if the row was deleted successfully, false otherwise
     * @throws SQLException if a database access error occurs
     */
        public static boolean deleteRowByID(Connection connection, String table, int id) throws SQLException {
            String sql = "DELETE FROM " + table + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                return statement.executeUpdate() > 0;
            }
        }

        /**
         * Deletes multiple rows from the specified table based on the given IDs.
         *
         * @param  connection the database connection
         * @param  table      the name of the table
         * @param  id         an array of IDs of the rows to be deleted
         * @return            true if all rows were deleted successfully, false otherwise
         * @throws SQLException if a database access error occurs
         */
    public static boolean deleteRowByID(Connection connection, String table, int[] id) throws SQLException {
            int count = 0;
            boolean out = true;
            while(count < id.length && out) {
                out = deleteRowByID(connection, table, id[count]);
            }

        return out;
    }
    /**
     * Creates a new table in the database with the specified name and columns.
     *
     * @param connection the database connection
     * @param nombreTabla the name of the table to create
     * @param columnas an array of column names for the new table
     * @param tipos an array of column types for the new table, corresponding to the columnas array
     * @return true if the table was created successfully, false otherwise
     * @throws SQLException if a database access error occurs
     * @throws IllegalArgumentException if the number of columnas and tipos arrays are not equal
     */
    public static boolean createTable(Connection connection, String nombreTabla, String[] columnas, String[] tipos) throws SQLException {
        if (columnas.length != tipos.length) {
            throw new IllegalArgumentException("El número de columnas y tipos debe ser el mismo.");
        }

        StringBuilder queryBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        queryBuilder.append(nombreTabla).append(" (");

        // Añadir columna ID_[nombreTabla] como PRIMARY KEY
        queryBuilder.append("ID_").append(nombreTabla).append(" INT AUTO_INCREMENT PRIMARY KEY");

        // Agregar las columnas proporcionadas
        for (int i = 0; i < columnas.length; i++) {
            queryBuilder.append(", ").append(columnas[i]).append(" ").append(tipos[i]);
        }

        queryBuilder.append(")");

        String query = queryBuilder.toString();

        try (Statement stmt = connection.createStatement()) {
            // Ejecutar la consulta para crear la tabla
            stmt.executeUpdate(query);
            return true; // Devolver true si la tabla se creó con éxito
        }
    }
    }

