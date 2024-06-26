import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The DatabaseManager class handles the connection to the MySQL database.
 */
public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/PoisePMS";
    private static final String USER = "otheruser";
    private static final String PASSWORD = "swordfish";

    /**
     * Gets the connection to the MySQL database.
     *
     * @return the connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
