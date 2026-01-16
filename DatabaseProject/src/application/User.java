package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {

    private int userId;
    private String userName;
    private String password;
    private String role;

    // ممكن يكونوا null بالـ DB → نخليهم Integer
    private Integer customerId; 
    private Integer staffId;

    public User(int userId, String userName, String password, String role,
    		Integer customerId, Integer staffId) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
        this.staffId = staffId;
    }

    // Constructor للإضافة (Insert)
    public User(String userName, String password, String role, Integer customerId, Integer staffId) throws SQLException {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
        this.staffId = staffId;
        addUserToDB();
    }

    private void addUserToDB() throws SQLException {
        String sql = "INSERT INTO users (username, password, role, customer_id, staff_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = Main.conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, password);
            stmt.setString(3, role);

            if (customerId == null) stmt.setNull(4, java.sql.Types.INTEGER);
            else stmt.setInt(4, customerId);

            if (staffId == null) stmt.setNull(5, java.sql.Types.INTEGER);
            else stmt.setInt(5, staffId);

            stmt.executeUpdate();
        }
        this.userId = getLastInsertedUserId();
    }

    private int getLastInsertedUserId() throws SQLException {
        String sql = "SELECT MAX(user_id) AS last_id FROM users";
        try (Statement stmt = Main.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("last_id");
        }
        return -1;
    }

    // ===== Getters =====
    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public Integer getCustomerId() { return customerId; }
    public Integer getStaffId() { return staffId; }

    // ===== Setters (مع Update DB) =====
    public void setUserName(String userName) throws SQLException {
        this.userName = userName;
        String sql = "UPDATE users SET username=? WHERE user_id=?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public void setPassword(String password) throws SQLException {
        this.password = password;
        String sql = "UPDATE users SET password=? WHERE user_id=?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(sql)) {
            stmt.setString(1, password);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public void setRole(String role) throws SQLException {
        this.role = role;
        String sql = "UPDATE users SET role=? WHERE user_id=?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public void setCustomerId(Integer customerId) throws SQLException {
        this.customerId = customerId;
        String sql = "UPDATE users SET customer_id=? WHERE user_id=?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(sql)) {
            if (customerId == null) stmt.setNull(1, java.sql.Types.INTEGER);
            else stmt.setInt(1, customerId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public void setStaffId(Integer staffId) throws SQLException {
        this.staffId = staffId;
        String sql = "UPDATE users SET staff_id=? WHERE user_id=?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(sql)) {
            if (staffId == null) stmt.setNull(1, java.sql.Types.INTEGER);
            else stmt.setInt(1, staffId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
}
