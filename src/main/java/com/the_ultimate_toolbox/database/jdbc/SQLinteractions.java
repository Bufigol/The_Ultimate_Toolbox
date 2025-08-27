package com.the_ultimate_toolbox.database.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class provides a set of static methods for interacting with a SQL database.
 */
public class SQLinteractions {
    private static final Logger logger = LogManager.getLogger(SQLinteractions.class);

    /**
     * Inserts a single record into the specified table.
     *
     * @param connection the database connection
     * @param table      the name of the table
     * @param columns    an array of column names
     * @param values     an array of values corresponding to the columns
     * @return true if the record was inserted successfully, false otherwise
     */
    public static boolean insertIntoTableOneRecord(Connection connection, String table, String[] columns, String[] values) {
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
        } catch (SQLException e) {
            logger.error("An error occurred while inserting a record", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a single record into the specified table with specified types.
     *
     * @param connection the database connection
     * @param table      the name of the table
     * @param columns    an array of column names
     * @param values     an array of values corresponding to the columns
     * @param types      an array of types corresponding to the columns
     * @return true if the record was inserted successfully, false otherwise
     */
    public static boolean insertIntoTableOneRecord(Connection connection, String table, String[] columns, String[] values, String[] types) {
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
                String type = types[i];
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
            }

            // Ejecuta la consulta
            int filasInsertadas = pstmt.executeUpdate();

            // Retorna true si al menos una fila fue insertada, de lo contrario retorna false
            return filasInsertadas > 0;
        } catch (SQLException e) {
            logger.error("An error occurred while inserting a record", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts multiple records into the specified table.
     *
     * @param connection the database connection
     * @param table      the name of the table
     * @param columns    an array of column names
     * @param values     a 2-dimensional array of values corresponding to the columns
     * @return the number of rows inserted, or 0 if no rows were inserted
     */
    public static int insertIntoTableMulipleRecords(Connection connection, String table, String[] columns, String[][] values) {
        int i = 0;
        for (String[] value : values) {
            if (insertIntoTableOneRecord(connection, table, columns, value)) {
                i++;
            }
        }
        return i;
    }

    /**
     * Inserts multiple records into the specified table.
     *
     * @param connection the database connection
     * @param table      the name of the table
     * @param columns    an array of column names
     * @param values     a 2-dimensional array of values corresponding to the columns
     * @param types      an array of types corresponding to the columns
     * @return the number of rows inserted, or 0 if no rows were inserted
     */
    public static int insertIntoTableMulipleRecords(Connection connection, String table, String[] columns, String[][] values, String[] types) {
        int i = 0;
        for (String[] value : values) {
            if (insertIntoTableOneRecord(connection, table, columns, value, types)) {
                i++;
            }
        }
        return i;
    }

    /**
     * Counts the number of rows in the specified table.
     *
     * @param connection the database connection
     * @param table      the name of the table
     * @return the number of rows in the table, or 0 if the table is empty
     */
    public static int countRows(Connection connection, String table) {
        String sql = "SELECT COUNT(*) FROM " + table;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            logger.error("An error occurred while counting rows", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a row from the specified table based on the given ID.
     *
     * @param connection the database connection
     * @param table      the name of the table
     * @param id         the ID of the row to be deleted
     * @return true if the row was deleted successfully, false otherwise
     */
    public static boolean deleteRowByID(Connection connection, String table, int id) {
        String sql = "DELETE FROM " + table + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("An error occurred while deleting a row", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes multiple rows from the specified table based on the given IDs.
     *
     * @param connection the database connection
     * @param table      the name of the table
     * @param ids        an array of IDs of the rows to be deleted
     * @return true if all rows were deleted successfully, false otherwise
     */
    public static boolean deleteRowsByID(Connection connection, String table, int[] ids) {
        boolean allDeleted = true;
        for (int id : ids) {
            if (!deleteRowByID(connection, table, id)) {
                allDeleted = false;
            }
        }
        return allDeleted;
    }

    /**
     * Updates a record in the specified table by ID.
     *
     * @param connection   the database connection
     * @param table        the name of the table
     * @param idColumnName the name of the ID column
     * @param id           the ID of the record to update
     * @param columns      an array of column names to update
     * @param values       an array of values corresponding to the columns
     * @param types        an array of data types corresponding to the columns
     * @return true if the record was updated successfully, false otherwise
     * @throws RuntimeException if an SQL exception occurs during the update
     */
    public static boolean updateById(Connection connection, String table, String idColumnName, int id, String[] columns, String[] values, String[] types) {
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        for (int i = 0; i < columns.length; i++) {
            sql.append(columns[i]).append(" = ?");
            if (i < columns.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE ").append(idColumnName).append(" = ?");
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < types.length; i++) {
                switch (types[i]) {
                    case "INT":
                        statement.setInt(i + 1, Integer.parseInt(values[i]));
                        break;
                    case "FLOAT":
                        statement.setFloat(i + 1, Float.parseFloat(values[i]));
                        break;
                    case "DOUBLE":
                        statement.setDouble(i + 1, Double.parseDouble(values[i]));
                        break;
                    default:
                        statement.setString(i + 1, values[i]);
                        break;
                }
            }
            statement.setInt(columns.length + 1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("An error occurred while updating a record", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Searches for records in a table by a specific field and value.
     *
     * @param connection the database connection
     * @param table      the name of the table
     * @param field      the name of the field to search in
     * @param value      the value to search for
     * @return an ArrayList of String arrays, where each String array represents a record
     */
    public static ArrayList<String[]> searchByField(Connection connection, String table, String field, String value) {
        String sql = "SELECT * FROM " + table + " WHERE " + field + " = ?";
        ArrayList<String[]> out = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, value);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int columnCount = rs.getMetaData().getColumnCount();
                    String[] row = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row[i] = rs.getString(i + 1);
                    }
                    out.add(row);
                }
            }
        } catch (SQLException e) {
            logger.error("An error occurred while searching by field", e);
            throw new RuntimeException(e);
        }

        return out;
    }

    /**
     * Searches for records in a table by multiple fields and values using the AND operator.
     *
     * @param connection the database connection
     * @param table      the name of the table
     * @param field      an array of field names to search in
     * @param values     an array of values to search for
     * @param types      an array of types for the values
     * @return an ArrayList of String arrays, where each String array represents a record
     */
    public static ArrayList<String[]> searchByMultipleFieldAND(Connection connection, String table, String[] field, String[] values, String[] types) {
        ArrayList<String[]> out = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + table + " WHERE ");
        if (field.length != values.length) {
            throw new IllegalArgumentException("The number of fields and values must be the same.");
        }
        for (int i = 0; i < field.length; i++) {
            sql.append(field[i]).append(" = ?");
            if (i < field.length - 1) {
                sql.append(" AND ");
            }
        }
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.length; i++) {
                String type = types[i];
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
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int columnCount = rs.getMetaData().getColumnCount();
                    String[] row = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row[i] = rs.getString(i + 1);
                    }
                    out.add(row);
                }
            }
        } catch (SQLException e) {
            logger.error("An error occurred while searching by multiple fields (AND)", e);
            throw new RuntimeException(e);
        }
        return out;
    }

    /**
     * Searches for records in a table by multiple fields and values using the OR operator.
     *
     * @param connection the database connection
     * @param table      the name of the table
     * @param field      an array of field names to search in
     * @param types      an array of types for the values
     * @param values     an array of values to search for
     * @return an ArrayList of String arrays, where each String array represents a record
     */
    public static ArrayList<String[]> searchByMultipleFieldOR(Connection connection, String table, String[] field, String[] types, String[] values) {
        ArrayList<String[]> out = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + table + " WHERE ");
        if (field.length != values.length) {
            throw new IllegalArgumentException("The number of fields and values must be the same.");
        }
        for (int i = 0; i < field.length; i++) {
            sql.append(field[i]).append(" = ?");
            if (i < field.length - 1) {
                sql.append(" OR ");
            }
        }
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.length; i++) {
                switch (types[i]) {
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
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int columnCount = rs.getMetaData().getColumnCount();
                    String[] row = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row[i] = rs.getString(i + 1);
                    }
                    out.add(row);
                }
            }
        } catch (SQLException e) {
            logger.error("An error occurred while searching by multiple fields (OR)", e);
            throw new RuntimeException(e);
        }
        return out;
    }

    /**
     * Creates a new table in the database with the specified name and columns.
     *
     * @param connection  the database connection
     * @param nombreTabla the name of the table to create
     * @param columnas    an array of column names for the new table
     * @param tipos       an array of column types for the new table, corresponding to the columnas array
     * @return true if the table was created successfully, false otherwise
     * @throws IllegalArgumentException if the number of columnas and tipos arrays are not equal
     */
    public static boolean createTable(Connection connection, String nombreTabla, String[] columnas, String[] tipos) {
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
        } catch (SQLException e) {
            logger.error("An error occurred while creating a table", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Searches for records in a table by a specific field and value, ignoring case.
     *
     * @param connection  the database connection
     * @param tableName   the name of the table
     * @param columnName  the name of the column to search in
     * @param searchValue the value to search for
     * @return an ArrayList of String arrays, where each String array represents a record
     */
    public static ArrayList<String[]> searchIgnoreCase(Connection connection, String tableName, String columnName, String searchValue) {
        ArrayList<String[]> out = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE LOWER(" + columnName + ") = LOWER(?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, searchValue);
            try (ResultSet resultSet = statement.executeQuery()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    String[] row = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row[i] = resultSet.getString(i + 1);
                    }
                    out.add(row);
                }
            }
        } catch (SQLException e) {
            logger.error("An error occurred while searching ignore case", e);
        }
        return out;
    }

    /**
     * Searches for records in a table by multiple fields and values, ignoring case.
     *
     * @param connection   the database connection
     * @param tableName    the name of the table
     * @param columnNames  an array of column names to search in
     * @param searchValues an array of values to search for
     * @param columnTypes  an array of types for the values
     * @return an ArrayList of String arrays, where each String array represents a record
     */
    public static ArrayList<String[]> searchIgnoreCaseMultipleFields(Connection connection, String tableName, String[] columnNames, String[] searchValues, String[] columnTypes) {
        ArrayList<String[]> out = new ArrayList<>();
        if (columnNames.length != searchValues.length || columnNames.length != columnTypes.length) {
            throw new IllegalArgumentException("The number of column names, search values, and column types must be equal.");
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE ");

        for (int i = 0; i < columnNames.length; i++) {
            if (i > 0) {
                sql.append(" AND ");
            }
            sql.append("LOWER(").append(columnNames[i]).append(") LIKE LOWER(?)");
        }

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < searchValues.length; i++) {
                String columnType = columnTypes[i];
                String value = searchValues[i];
                if ("VARCHAR".equalsIgnoreCase(columnType) || "CHAR".equalsIgnoreCase(columnType)) {
                    statement.setString(i + 1, "%" + value + "%");
                } else {
                    statement.setString(i + 1, value);
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String[] row = new String[columnNames.length];
                    for (int i = 0; i < columnNames.length; i++) {
                        row[i] = resultSet.getString(columnNames[i]);
                    }
                    out.add(row);
                }
            }
        } catch (SQLException e) {
            logger.error("An error occurred while searching ignore case on multiple fields", e);
        }
        return out;
    }
}