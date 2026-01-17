package application;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserStage {

    static MyTableView<User> userTable;

    private TableColumn<User, Integer> userId;
    private TableColumn<User, String> userName, password, role;

    private Button add, update, remove;
    private TextField search;
    private Label searchByUserName;
    private VBox all;

    public UserStage() {

        userTable = new MyTableView<>();

        userId   = userTable.createStyledColumn("User Id", "userId", Integer.class);
        userName = userTable.createStyledColumn("User Name", "userName");
        password = userTable.createStyledColumn("Password", "password");
        role     = userTable.createStyledColumn("Role", "role");

        userTable.getColumns().addAll(userId, userName, password, role);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTable.setMinHeight(500);
        userTable.setMaxWidth(700);
        userTable.setItems(Main.users);

        // ========= Buttons =========

        add = new MyButton("➕ Add", 2);
        add.setOnAction(e -> openAddUserStage());

        update = new MyButton("✎ Update", 2);
        update.setOnAction(e -> openUpdateUserStage());

        remove = new MyButton("➖ Delete", 2);
        remove.setOnAction(e -> deleteSelectedUser());

        HBox buttons = new HBox(10, add, update, remove);
        buttons.setAlignment(Pos.CENTER);

        // ========= Search =========

        searchByUserName = new MyLabel("Search By User Name : ");
        search = new MyTextField();

        HBox searchBox = new HBox(10, searchByUserName, search);
        searchBox.setAlignment(Pos.CENTER);

        search.setOnKeyTyped(e -> {
            String s = search.getText();
            if (s == null || s.isEmpty()) {
                userTable.setItems(Main.users);
                return;
            }
            ObservableList<User> temp = FXCollections.observableArrayList();
            for (User u : Main.users) {
                if (u.getUserName().toLowerCase().startsWith(s.toLowerCase())) {
                    temp.add(u);
                }
            }
            userTable.setItems(temp.isEmpty() ? Main.users : temp);
        });

        all = new VBox(10, searchBox, buttons, userTable);
        all.setAlignment(Pos.CENTER);
    }

    // =================== Add ===================

    private void openAddUserStage() {
        Label title = new MyLabel("Add User", 1);

        Label userNameL = new MyLabel("User Name : ");
        TextField userNameTF = new MyTextField();

        Label passwordL = new MyLabel("Password : ");
        TextField passwordTF = new MyTextField();

        Label roleL = new MyLabel("Role : ");
        MyComboBox<String> roleCB = new MyComboBox<>("admin", "staff", "customer");

        // ✅ حقول IDs (بتظهر حسب الدور)
        Label customerIdL = new MyLabel("Customer Id : ");
        TextField customerIdTF = new MyTextField();

        Label staffIdL = new MyLabel("Staff Id : ");
        TextField staffIdTF = new MyTextField();

        // بالبداية مخفيين
        customerIdL.setVisible(false); customerIdTF.setVisible(false);
        staffIdL.setVisible(false); staffIdTF.setVisible(false);

        // لما يتغير الدور
        roleCB.setOnAction(ev -> {
            String r = roleCB.getValue();

            boolean isCustomer = "customer".equalsIgnoreCase(r);
            boolean isStaff = "staff".equalsIgnoreCase(r);

            customerIdL.setVisible(isCustomer);
            customerIdTF.setVisible(isCustomer);

            staffIdL.setVisible(isStaff);
            staffIdTF.setVisible(isStaff);

            // تنظيف
            customerIdTF.clear();
            staffIdTF.clear();
        });

        GridPane g = new GridPane();
        g.setVgap(8);
        g.setHgap(8);
        g.setAlignment(Pos.CENTER);

        g.add(userNameL, 0, 0); g.add(userNameTF, 1, 0);
        g.add(passwordL, 0, 1); g.add(passwordTF, 1, 1);
        g.add(roleL, 0, 2);     g.add(roleCB, 1, 2);

        g.add(customerIdL, 0, 3); g.add(customerIdTF, 1, 3);
        g.add(staffIdL, 0, 4);    g.add(staffIdTF, 1, 4);

        Button clear = new MyButton("Clear", 2);
        Button addBtn = new MyButton("Add", 2);

        HBox buttons = new HBox(10, addBtn, clear);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(12, title, g, buttons);
        root.setAlignment(Pos.CENTER);

        Stage st = new Stage();
        st.setTitle("Add User");
        st.setScene(new Scene(root, 600, 520));
        st.show();

        addBtn.setOnAction(ee -> {
            String un = userNameTF.getText();
            if (un == null || un.isEmpty()) {
                Main.notValidAlert("Not Valid Input", "User name is empty");
                return;
            }
            for (User u : Main.users) {
                if (u.getUserName().equalsIgnoreCase(un)) {
                    Main.notValidAlert("Not Valid Input", "User name already exists");
                    return;
                }
            }

            String pw = passwordTF.getText();
            if (pw == null || pw.isEmpty()) {
                Main.notValidAlert("Not Valid Input", "Password can't be empty");
                return;
            }

            String r = roleCB.getValue();
            if (r == null || r.isEmpty()) {
                Main.notValidAlert("Not Valid Input", "Select role for user");
                return;
            }

            Integer customerId = null;
            Integer staffId = null;

            try {
                if ("customer".equalsIgnoreCase(r)) {
                    String s = customerIdTF.getText();
                    if (s == null || s.trim().isEmpty()) {
                        Main.notValidAlert("Not Valid Input", "Customer Id is required");
                        return;
                    }
                    customerId = Integer.parseInt(s.trim());
                } else if ("staff".equalsIgnoreCase(r)) {
                    String s = staffIdTF.getText();
                    if (s == null || s.trim().isEmpty()) {
                        Main.notValidAlert("Not Valid Input", "Staff Id is required");
                        return;
                    }
                    staffId = Integer.parseInt(s.trim());
                }

                // ✅ استدعاء الكونستركتر الصح
                User u = new User(un, pw, r, customerId, staffId);
                Main.users.add(u);
                Main.validAlert("User Added", "User added successfully");
                st.close();

            } catch (NumberFormatException ex) {
                Main.notValidAlert("Not Valid Input", "Id must be a number");
            } catch (SQLException ex) {
                Main.notValidAlert("DB Error", ex.getMessage());
            }
        });

        clear.setOnAction(ee -> {
            userNameTF.clear();
            passwordTF.clear();
            roleCB.setValue(null);
            customerIdTF.clear();
            staffIdTF.clear();
            customerIdL.setVisible(false); customerIdTF.setVisible(false);
            staffIdL.setVisible(false); staffIdTF.setVisible(false);
        });
    }
    // =================== Update ===================

    private void openUpdateUserStage() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Main.notValidAlert("Nothing Selected", "Select a user to update");
            return;
        }

        Label title = new MyLabel("Update User", 1);

        Label userNameL = new MyLabel("User Name : ");
        TextField userNameTF = new MyTextField();
        userNameTF.setText(selected.getUserName());

        Label passwordL = new MyLabel("Password : ");
        TextField passwordTF = new MyTextField();
        passwordTF.setText(selected.getPassword());

        Label roleL = new MyLabel("Role : ");
        MyComboBox<String> roleCB = new MyComboBox<>("admin", "staff", "customer");
        roleCB.setValue(selected.getRole());

        GridPane g = new GridPane();
        g.addColumn(0, userNameL, passwordL, roleL);
        g.addColumn(1, userNameTF, passwordTF, roleCB);
        g.setVgap(8);
        g.setHgap(8);
        g.setAlignment(Pos.CENTER);

        Button clear = new MyButton("Clear", 2);
        Button updateBtn = new MyButton("Update", 2);

        HBox buttons = new HBox(10, updateBtn, clear);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(12, title, g, buttons);
        root.setAlignment(Pos.CENTER);

        Stage st = new Stage();
        st.setTitle("Update User");
        st.setScene(new Scene(root, 600, 500));
        st.show();

        updateBtn.setOnAction(ee -> {
            String un = userNameTF.getText();
            if (un == null || un.isEmpty()) {
                Main.notValidAlert("Not Valid Input", "User name is empty");
                return;
            }

            if (!un.equalsIgnoreCase(selected.getUserName())) {
                for (User u : Main.users) {
                    if (u.getUserName().equalsIgnoreCase(un)) {
                        Main.notValidAlert("Not Valid Input", "User name already exists");
                        return;
                    }
                }
            }

            String pw = passwordTF.getText();
            if (pw == null || pw.isEmpty()) {
                Main.notValidAlert("Not Valid Input", "Password can't be empty");
                return;
            }

            String r = roleCB.getValue();
            if (r == null || r.isEmpty()) {
                Main.notValidAlert("Not Valid Input", "Select role for user");
                return;
            }

            try {
                selected.setUserName(un);
                selected.setPassword(pw);
                selected.setRole(r);
                userTable.refresh();
                Main.validAlert("User Updated", "User updated successfully");
                st.close();
            } catch (SQLException ex) {
                Main.notValidAlert("DB Error", ex.getMessage());
            }
        });

        clear.setOnAction(ee -> {
            userNameTF.clear();
            passwordTF.clear();
            roleCB.setValue(null);
        });
    }

    // =================== Delete ===================

    private void deleteSelectedUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Main.notValidAlert("Nothing Selected", "Select a user to delete");
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Remove User");
        confirm.setHeaderText(null);
        confirm.setContentText("Remove user with id " + selected.getUserId() + " ?");

        ButtonType res = confirm.showAndWait().orElse(ButtonType.CANCEL);
        if (res != ButtonType.OK) return;

        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = Main.conn.prepareStatement(sql)) {
            stmt.setInt(1, selected.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.notValidAlert("DB Error", e.getMessage());
            return;
        }

        Main.users.remove(selected);
        Main.validAlert("User Removed", "User removed successfully");
    }

    // =================== Getters ===================

    public TableView<User> getUserTable() { return userTable; }
    public VBox getAll() { return all; }

<<<<<<< HEAD
   
    public void showStage() {
        Stage st = new Stage();
        st.setTitle("Users");
        st.setScene(new Scene(all, 800, 700));
        st.show();
    }
}
=======
    // لو بدك تفتحيه كـ Stage:
    public void showStage() {
        Stage st = new Stage();
        st.setTitle("Users");
        st.setScene(new Scene(all, 800, 700));
        st.show();
    }
}
>>>>>>> branch 'main' of https://github.com/sajahammad2005/lady90s
