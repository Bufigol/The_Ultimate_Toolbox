import com.bufigol.database.GenericDBConnector; // Importing the GenericDBConnector class from the com.bufigol.database package

public class test {
    
    public static void main(String[] args) {
        // Create a new instance of the GenericDBConnector class, which is used to connect to a MySQL database
        GenericDBConnector db = new GenericDBConnector(
            "jdbc:mysql://localhost:3306/vinosproject", // The JDBC URL for the MySQL database, including the hostname, port number, database name
            "root", // The username used to connect to the database
            "admin" // The password used to connect to the database
        );
    }
}
