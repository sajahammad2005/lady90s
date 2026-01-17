package application;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
<<<<<<< HEAD
    private static final String url = "jdbc:mysql://127.0.0.1:3306/lady90s";
=======
    private static final String url = "jdbc:mysql://62.84.184.87:3306/lady90s";
>>>>>>> branch 'main' of https://github.com/sajahammad2005/lady90s
    private static final String name = "root";
<<<<<<< HEAD
    private static final String password = "rand123";
=======
    private static final String password = "iRoot137!";
>>>>>>> branch 'main' of https://github.com/sajahammad2005/lady90s

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