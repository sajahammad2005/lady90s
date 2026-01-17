package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.stage.Stage;

public class Main extends Application {

    public static Connection conn = DBConnect.getConnection();

    public static ObservableList<User> users = FXCollections.observableArrayList();

    public static ObservableList<Customer> customers = FXCollections.observableArrayList();
    public static ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    public static ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();
    public static ObservableList<Product> products = FXCollections.observableArrayList();
    public static ObservableList<ProductVariant> variants = FXCollections.observableArrayList();
    public static ObservableList<Inventory> inventory = FXCollections.observableArrayList();
// rr
    @Override
    public void start(Stage primaryStage) {
        try {
            if (conn != null) System.out.println("Connected!");
            else System.out.println("X");
            loadUsers();

            loadCustomers();
            loadSuppliers();
            loadWarehouses();
            loadProducts();
            loadProductVariants();
            loadInventory();

            new LoginScene().getLoginStage().show();

        } catch (Exception e) {
            e.printStackTrace();
            notValidAlert("Error", e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static void loadCustomers() {
        customers.clear();
        String sql = "SELECT * FROM customer"; 

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");

                customers.add(new Customer(id, name, phone, email, address));
            }

        } catch (SQLException e) {
            notValidAlert("Database Error (Customers)", e.getMessage());
        }
    }

    public static void loadSuppliers() {
        suppliers.clear();
        String sql = "SELECT * FROM supplier"; // ✅ حسب جدولك

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("supplier_id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");

                suppliers.add(new Supplier(id, name, phone, email, address));
            }

        } catch (SQLException e) {
            notValidAlert("Database Error (Suppliers)", e.getMessage());
        }
    }

    public static void loadWarehouses() {
        warehouses.clear();

        String sql = "SELECT warehouse_id, name, branch_id, location FROM warehouse"; // عدلي اسم الجدول إذا مختلف

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int warehouseId = rs.getInt("warehouse_id");
                String name = rs.getString("name");

                Integer branchId = rs.getInt("branch_id");
                if (rs.wasNull()) branchId = null;   // لأنه Integer مش int

                String location = rs.getString("location");

                warehouses.add(new Warehouse(warehouseId, name, branchId, location));
            }

        } catch (SQLException e) {
            notValidAlert("Database Error (Warehouses)", e.getMessage());
        }
    }


    public static void loadProducts() {
        products.clear();
        String sql = "SELECT * FROM product"; // ✅ حسب جدولك بالصورة

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                String brand = rs.getString("brand");
                double basePrice = rs.getDouble("base_price");
                String description = rs.getString("description");

                products.add(new Product(id, name, category, brand, basePrice, description));
            }

        } catch (SQLException e) {
            notValidAlert("Database Error (Products)", e.getMessage());
        }
    }

    public static void loadProductVariants() {
        variants.clear();
        String sql = "SELECT * FROM productvariant"; 

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int variantId = rs.getInt("variant_id");
                int productId = rs.getInt("product_id");
                String color = rs.getString("color");
                String size = rs.getString("size");
                String material = rs.getString("material");
                double addPrice = rs.getDouble("additional_price");

                variants.add(new ProductVariant(variantId, productId, color, size, material, addPrice));
            }
            

        } catch (SQLException e) {
            notValidAlert("Database Error (Variants)", e.getMessage());
        }
    }

    public static void loadInventory() {
        inventory.clear();
        String sql = "SELECT * FROM inventory";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int inventoryId = rs.getInt("inventory_id");
                int variantId = rs.getInt("variant_id");
                int warehouseId = rs.getInt("warehouse_id");
                int quantity = rs.getInt("quantity");
                java.sql.Date lastUpdated = rs.getDate("last_updated");

                inventory.add(new Inventory(inventoryId, variantId, warehouseId, quantity, lastUpdated));
            }

        } catch (SQLException e) {
            notValidAlert("Database Error (Inventory)", e.getMessage());
        }
    }

    public static void notValidAlert(String title, String content) {
        Alert v = new Alert(AlertType.ERROR);
        v.setTitle(title);
        v.setContentText(content);
//        ImageView i = new ImageView(new Image("false.png"));
//        i.setFitHeight(50);
//        i.setFitWidth(50);
//        v.setGraphic(i);
        v.setHeaderText(null);
        v.showAndWait();
    }
    public static void loadUsers() {
        users.clear();
        String sql = "SELECT * FROM users";  

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                Integer staffId = rs.getInt("staff_id");
                if (rs.wasNull()) staffId = null;

                Integer customerId = rs.getInt("customer_id");
                if (rs.wasNull()) customerId = null;

                users.add(new User(userId, username, password, role, staffId, customerId));
            }

            if (UserStage.userTable != null) {
                UserStage.userTable.setItems(users);
            }

        } catch (SQLException e) {
            notValidAlert("Database Error (Users)", e.getMessage());
        }
    }


    public static void validAlert(String title, String content) {
        Alert v = new Alert(AlertType.INFORMATION);
        v.setTitle(title);
        v.setContentText(content);
//        ImageView i = new ImageView(new Image("true.png"));
//        i.setFitHeight(50);
//        i.setFitWidth(50);
//        v.setGraphic(i);
        v.setHeaderText(null);
        v.showAndWait();
    }
}