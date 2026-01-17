package application;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
    private static final String url = "jdbc:mysql://62.84.184.87:3306/lady90s";
    private static final String name = "root";
    private static final String password = "iRoot137!";

    public DBConnect() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, name, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
//