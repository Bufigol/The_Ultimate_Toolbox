import com.bufigol.database.GenericDBConnector;

public class test {
    public static void main(String[] args) {
        GenericDBConnector db = new GenericDBConnector("jdbc:mysql://localhost:3306/vinosproject", "root", "admin");
    }
}
