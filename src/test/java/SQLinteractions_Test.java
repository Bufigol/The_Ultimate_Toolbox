import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bufigol.database.GenericDBConnector;
import com.bufigol.database.SQLinteractions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SQLinteractions_Test {
    // Mock de una conexión de base de datos para las pruebas
    private static GenericDBConnector connection = null;

    @Test
    public void testInsertIntoTableOneRecord_Success() throws SQLException {
        String table = "test_table";
        String[] columns = {"column1", "column2", "column3"};
        String[] values = {"value1", "value2", "value3"};

        assertTrue(SQLinteractions.insertIntoTableOneRecord(connection.getConnection(), table, columns, values));
        // Agregar más aserciones si es necesario para verificar la inserción correcta en la base de datos
    }

    @Test
    public void testInsertIntoTableOneRecord_ColumnsValuesMismatch() {
        String table = "test_table";
        String[] columns = {"column1", "column2"};
        String[] values = {"value1", "value2", "value3"}; // Más valores que columnas

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SQLinteractions.insertIntoTableOneRecord(connection.getConnection(), table, columns, values);
        });

        assertEquals("The number of columns and values must be the same.", exception.getMessage());
    }
}
