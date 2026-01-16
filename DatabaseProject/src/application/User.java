package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {

    private int userId;
    private String userName;
    private String password;
    private String role; // admin / staff / customer

    // بدائل عن employeeId
    private Integer staffId;     // nullable
    private Integer customerId;  // nullable

    public User(int userId, String userName, String password, String role, Integer staffId, Integer customerId) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.staffId = staffId;
        this.customerId = customerId;
    }

    // لإنشاء User جديد + إدخاله على DB
    public User(String userName, String password, String role, Integer staffId, Integer customerId) throws SQLException {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.staffId = staffId;
        this.customerId = customerId;

        validateRoleLinks();
        addUserToDB();
    }

    private void validateRoleLinks() {
        // admin: لازم الاثنين null
        if ("admin".equalsIgnoreCase(role)) {
            staffId = null;
            customerId = null;
            return;
        }

        // staff: لازم staffId موجود و customerId null
        if ("staff".equalsIgnoreCase(role)) {
            if (staffId == null) throw new IllegalArgumentException("staffId is required for role=staff");
            customerId = null;
            return;
        }

        // customer: لازم customerId موجود و staffId null
        if ("customer".equalsIgnoreCase(role)) {
            if (customerId == null) throw new IllegalArgumentException("customerId is required for role=customer");
            staffId = null;
            return;
        }

        throw new IllegalArgumentException("Invalid role: " + role);
    }

    public void addUserToDB() throws SQLException {
        String sql = "INSERT INTO users (username, password, role, staff_id, customer_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = Main.conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, password);
            stmt.setString(3, role);

            if (staffId == null) stmt.setNull(4, java.sql.Types.INTEGER);
            else stmt.setInt(4, staffId);

            if (customerId == null) stmt.setNull(5, java.sql.Types.INTEGER);
            else stmt.setInt(5, customerId);

            stmt.executeUpdate();
        }

        this.userId = getLastInsertedUserId();
    }

    public int getLastInsertedUserId() throws SQLException {
        String sql = "SELECT MAX(user_id) AS last_id FROM users";
        try (Statement stmt = Main.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt("last_id");
            return -1;
        }
    }

    // ✅ لوجين صح من DB (بدل مشاكل List index out of bounds)
    public static User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT user_id, username, password, role, staff_id, customer_id " +
                     "FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = Main.conn.prepareStatement(sql)) {
            stmt.setString(1, username.trim());
            stmt.setString(2, password.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;

                int id = rs.getInt("user_id");
                String un = rs.getString("username");
                String pw = rs.getString("password");
                String role = rs.getString("role");

                Integer staffId = (Integer) rs.getObject("staff_id");
                Integer customerId = (Integer) rs.getObject("customer_id");

                return new User(id, un, pw, role, staffId, customerId);
            }
        }
    }

    // ===== Getters / Setters (مع تحديث DB) =====

    public int getUserId() { return userId; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) throws SQLException {
        this.userName = userName;
        String update = "UPDATE users SET username = ? WHERE user_id = ?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(update)) {
            stmt.setString(1, userName);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public String getPassword() { return password; }

    public void setPassword(String password) throws SQLException {
        this.password = password;
        String update = "UPDATE users SET password = ? WHERE user_id = ?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(update)) {
            stmt.setString(1, password);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public String getRole() { return role; }

    public void setRole(String role) throws SQLException {
        this.role = role;
        validateRoleLinks(); // يظبط staff/customer ids حسب الدور

        String update = "UPDATE users SET role = ?, staff_id = ?, customer_id = ? WHERE user_id = ?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(update)) {
            stmt.setString(1, this.role);

            if (staffId == null) stmt.setNull(2, java.sql.Types.INTEGER);
            else stmt.setInt(2, staffId);

            if (customerId == null) stmt.setNull(3, java.sql.Types.INTEGER);
            else stmt.setInt(3, customerId);

            stmt.setInt(4, userId);
            stmt.executeUpdate();
        }
    }

    public Integer getStaffId() { return staffId; }

    public void setStaffId(Integer staffId) throws SQLException {
        this.staffId = staffId;
        if (!"staff".equalsIgnoreCase(role)) throw new IllegalArgumentException("Cannot set staffId unless role=staff");

        String update = "UPDATE users SET staff_id = ? WHERE user_id = ?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(update)) {
            if (staffId == null) stmt.setNull(1, java.sql.Types.INTEGER);
            else stmt.setInt(1, staffId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public Integer getCustomerId() { return customerId; }

    public void setCustomerId(Integer customerId) throws SQLException {
        this.customerId = customerId;
        if (!"customer".equalsIgnoreCase(role)) throw new IllegalArgumentException("Cannot set customerId unless role=customer");

        String update = "UPDATE users SET customer_id = ? WHERE user_id = ?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(update)) {
            if (customerId == null) stmt.setNull(1, java.sql.Types.INTEGER);
            else stmt.setInt(1, customerId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
}
