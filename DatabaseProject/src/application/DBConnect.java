package application;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
    private static final String url = "jdbc:mysql://127.0.0.1:3307/lady90s";
    private static final String name = "root";
    private static final String password = "123456789";

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