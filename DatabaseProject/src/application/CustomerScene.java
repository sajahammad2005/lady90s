package application;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomerScene {
    private Stage stage;
    private TableView<Customer> table;
    
    public CustomerScene(Stage stage) {
        this.stage = stage;
    }
    
    public Scene createScene() {
        BorderPane root = new BorderPane();
        
        VBox mainContainer = new VBox(0);
        mainContainer.setMaxWidth(1100);
        mainContainer.setAlignment(Pos.CENTER);
        BorderPane.setMargin(mainContainer, new Insets(30, 50, 30, 50));
   //     
        VBox header = createHeader();
        
        VBox tableContainer = createTableContainer();
        
        mainContainer.getChildren().addAll(header, tableContainer);
        root.setCenter(mainContainer);
        
        loadCustomers();
        
        Scene scene = new Scene(root, 1530, 785);
        return scene;
    }
    
    private VBox createHeader() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 30, 0));
        
        Label title = new Label("Customer Management");
        title.setStyle(
            "-fx-font-size: 42px; " +
            "-fx-font-weight: 900; " +
            "-fx-text-fill: black; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 4);"
        );
        
        Rectangle line = new Rectangle(0, 4);
        line.setFill(Color.BLACK);
        line.setArcWidth(4);
        line.setArcHeight(4);
        line.setEffect(new DropShadow(10, Color.rgb(255, 255, 255, 0.5)));
        
        title.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            line.setWidth(newBounds.getWidth());
        });
        
        ScaleTransition st = new ScaleTransition(Duration.seconds(1.5), line);
        st.setFromX(1.0);
        st.setToX(1.05);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
        header.getChildren().addAll(title, line);
        return header;
    }
    
    private VBox createTableContainer() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95); " +
            "-fx-background-radius: 24; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 40, 0, 0, 10);"
        );
        
        // Table - create first
        table = createModernTable();
        
        // Search and action bar - create after table
        HBox actionBar = createActionBar();
        
        // Button Panel
        HBox buttonPanel = createButtonPanel();
        
        container.getChildren().addAll(actionBar, table, buttonPanel);
        return container;
    }
    
    private HBox createActionBar() {
        HBox actionBar = new HBox(15);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        actionBar.setPadding(new Insets(0, 0, 10, 0));
        
        // Search field with icon
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search customers...");
        searchField.setPrefWidth(350);
        searchField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-background-color: #f8f9fa; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: transparent; " +
            "-fx-border-radius: 12; " +
            "-fx-padding: 12 20; " +
            "-fx-prompt-text-fill: #9ca3af;"
        );
        
        searchField.focusedProperty().addListener((obs, old, focused) -> {
            if (focused) {
                searchField.setStyle(
                    "-fx-font-size: 14px; " +
                    "-fx-background-color: white; " +
                    "-fx-background-radius: 12; " +
                    "-fx-border-color: #667eea; " +
                    "-fx-border-radius: 12; " +
                    "-fx-border-width: 2; " +
                    "-fx-padding: 12 20; " +
                    "-fx-prompt-text-fill: #9ca3af; " +
                    "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.3), 12, 0, 0, 0);"
                );
            } else {
                searchField.setStyle(
                    "-fx-font-size: 14px; " +
                    "-fx-background-color: #f8f9fa; " +
                    "-fx-background-radius: 12; " +
                    "-fx-border-color: transparent; " +
                    "-fx-border-radius: 12; " +
                    "-fx-padding: 12 20; " +
                    "-fx-prompt-text-fill: #9ca3af;"
                );
            }
        });
        
        // Stats label
        Label statsLabel = new Label("📊 Total Customers: 0");
        statsLabel.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 600; " +
            "-fx-text-fill: #6b7280; " +
            "-fx-padding: 8 16; " +
            "-fx-background-color: #f3f4f6; " +
            "-fx-background-radius: 10;"
        );
        
        table.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends Customer> c) -> {
            statsLabel.setText("📊 Total Customers: " + table.getItems().size());
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        actionBar.getChildren().addAll(searchField, spacer, statsLabel);
        return actionBar;
    }
    
    private TableView<Customer> createModernTable() {
        TableView<Customer> tableView = new TableView<>();
        tableView.setPrefHeight(350);
        tableView.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-table-cell-border-color: transparent; " +
            "-fx-background-radius: 16; " +
            "-fx-border-radius: 16;"
        );
        
        // Custom table styling
        tableView.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>() {
                @Override
                protected void updateItem(Customer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setStyle("");
                    } else {
                        setStyle(
                            "-fx-background-color: white; " +
                            "-fx-border-color: #f3f4f6; " +
                            "-fx-border-width: 0 0 1 0; " +
                            "-fx-padding: 12 0;"
                        );
                    }
                }
            };
            
            row.hoverProperty().addListener((obs, wasHovered, isHovered) -> {
                if (isHovered && !row.isEmpty()) {
                    row.setStyle(
                        "-fx-background-color: #f9fafb; " +
                        "-fx-border-color: #e5e7eb; " +
                        "-fx-border-width: 0 0 1 0; " +
                        "-fx-padding: 12 0; " +
                        "-fx-cursor: hand;"
                    );
                } else if (!row.isEmpty()) {
                    row.setStyle(
                        "-fx-background-color: white; " +
                        "-fx-border-color: #f3f4f6; " +
                        "-fx-border-width: 0 0 1 0; " +
                        "-fx-padding: 12 0;"
                    );
                }
            });
            
            return row;
        });
        
        TableColumn<Customer, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("cId"));
        idCol.setPrefWidth(70);
        styleModernColumn(idCol, "#667eea");
        
        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);
        styleModernColumn(nameCol, "#10b981");
        
        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone Number");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(160);
        styleModernColumn(phoneCol, "#f59e0b");
        
        TableColumn<Customer, String> emailCol = new TableColumn<>("Email Address");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(240);
        styleModernColumn(emailCol, "#8b5cf6");
        
        TableColumn<Customer, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setPrefWidth(340);
        styleModernColumn(addressCol, "#ef4444");
        
        tableView.getColumns().addAll(idCol, nameCol, phoneCol, emailCol, addressCol);
        
        return tableView;
    }
    
    private void styleModernColumn(TableColumn<?, ?> column, String accentColor) {
        column.setStyle(
            "-fx-font-size: 13px; " +
            "-fx-font-weight: 700; " +
            "-fx-text-fill: #1f2937; " +
            "-fx-alignment: CENTER_LEFT; " +
            "-fx-background-color: #f9fafb; " +
            "-fx-padding: 16 12;"
        );
        
        Label headerLabel = new Label(column.getText());
        headerLabel.setStyle(
            "-fx-font-size: 13px; " +
            "-fx-font-weight: 700; " +
            "-fx-text-fill: " + accentColor + "; " +
            "-fx-padding: 4 0;"
        );
        column.setGraphic(headerLabel);
        column.setText("");
    }
    
    private HBox createButtonPanel() {
        HBox buttonPanel = new HBox(12);
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.setPadding(new Insets(10, 0, 0, 0));
        
        Button addBtn = createModernButton("➕ Add Customer", 
             "#065f46");
        Button updateBtn = createModernButton("✏️ Update",   "#1e40af");
        Button deleteBtn = createModernButton("🗑️ Delete", "#991b1b");
        Button refreshBtn = createModernButton("🔄 Refresh", "#5b21b6");
        
        addBtn.setOnAction(e -> {
            playButtonAnimation(addBtn);
            showAddDialog();
        });
        updateBtn.setOnAction(e -> {
            playButtonAnimation(updateBtn);
            showUpdateDialog();
        });
        deleteBtn.setOnAction(e -> {
            playButtonAnimation(deleteBtn);
            deleteCustomer();
        });
        refreshBtn.setOnAction(e -> {
            playButtonAnimation(refreshBtn);
            loadCustomers();
        });
        
        buttonPanel.getChildren().addAll(addBtn, updateBtn, deleteBtn, refreshBtn);
        return buttonPanel;
    }
    
    private Button createModernButton(String text, String hoverColor) {
        Button btn = new Button(text);
        btn.setStyle(String.format(
            	"-fx-background-color: " + hoverColor + " ;" +

            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 700; " +
            "-fx-padding: 14 28; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);"
        ));
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle(String.format(
            	"-fx-background-color: " + hoverColor + " ;" +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: 700; " +
                "-fx-padding: 14 28; " +
                "-fx-background-radius: 12; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 4); " +
                "-fx-scale-x: 1.05; " +
                "-fx-scale-y: 1.05;"
                
            ));
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle(String.format(
                	"-fx-background-color: " + hoverColor + " ;" +

                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: 700; " +
                "-fx-padding: 14 28; " +
                "-fx-background-radius: 12; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);"
            ));
        });
        
        return btn;
    }
    
    private void playButtonAnimation(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(0.95);
        st.setToY(0.95);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }
    
    private void showAddDialog() {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");
        dialog.setHeaderText("📝 Enter Customer Information");
        
        ButtonType addButtonType = new ButtonType("Add Customer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        GridPane grid = createModernInputGrid();
        
        TextField idField = createModernTextField("Enter ID...");
        TextField nameField = createModernTextField("Enter full name...");
        TextField phoneField = createModernTextField("Enter phone number...");
        TextField emailField = createModernTextField("Enter email address...");
        TextField addressField = createModernTextField("Enter address...");
        
        grid.add(createModernLabel("Customer ID"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(createModernLabel("Full Name"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(createModernLabel("Phone Number"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(createModernLabel("Email Address"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(createModernLabel("Address"), 0, 4);
        grid.add(addressField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        dialog.setOnShown(e -> styleModernDialog(dialog.getDialogPane(), addButtonType));
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                	System.out.println("in");
                    int id = Integer.parseInt(idField.getText());
                    new Customer(id, nameField.getText(), phoneField.getText(), 
                               emailField.getText(), addressField.getText());
                    return null;
                } catch (NumberFormatException e) {
                    showModernError("Invalid Input", "Please enter a valid numeric ID.");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait();
        loadCustomers();
    }
    
    private void showUpdateDialog() {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showModernError("No Selection", "Please select a customer from the table to update.");
            return;
        }
        
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Update Customer");
        dialog.setHeaderText("✏️ Update Customer Information");
        
        ButtonType updateButtonType = new ButtonType("Update Customer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);
        
        GridPane grid = createModernInputGrid();
        
        TextField nameField = createModernTextField("Enter full name...");
        nameField.setText(selected.getName());
        TextField phoneField = createModernTextField("Enter phone number...");
        phoneField.setText(selected.getPhone());
        TextField emailField = createModernTextField("Enter email address...");
        emailField.setText(selected.getEmail());
        TextField addressField = createModernTextField("Enter address...");
        addressField.setText(selected.getAddress());
        
        grid.add(createModernLabel("Full Name"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(createModernLabel("Phone Number"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(createModernLabel("Email Address"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(createModernLabel("Address"), 0, 3);
        grid.add(addressField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        dialog.setOnShown(e -> styleModernDialog(dialog.getDialogPane(), updateButtonType));
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                selected.updateDB(selected.getCId(), nameField.getText(), 
                                phoneField.getText(), emailField.getText(), 
                                addressField.getText());
                return selected;
            }
            return null;
        });
        
        dialog.showAndWait();
        loadCustomers();
    }
    
    private void deleteCustomer() {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showModernError("No Selection", "Please select a customer from the table to delete.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("🗑️ Delete Customer");
        alert.setContentText("Are you sure you want to delete " + selected.getName() + "?\nThis action cannot be undone.");
        
        styleModernAlert(alert);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String sql = "DELETE FROM customer WHERE customer_id = " + selected.getCId();
                    Statement st = Main.conn.createStatement();
                    st.executeUpdate(sql);
                    loadCustomers();
                    showSuccess("Success", "Customer deleted successfully!");
                } catch (Exception e) {
                    showModernError("Delete Error", "Failed to delete customer from database.");
                }
            }
        });
    }
    
    private void loadCustomers() {
        table.getItems().clear();
        try {
            String sql = "SELECT * FROM customer";
            Statement st = Main.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                Customer c = new Customer();
                c.setcId(rs.getInt("customer_id"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                c.setAddress(rs.getString("address"));
                table.getItems().add(c);
            }
        } catch (Exception e) {
            showModernError("Load Error", "Failed to load customers from database.");
        }
    }
    
    private GridPane createModernInputGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(18);
        grid.setPadding(new Insets(30, 30, 20, 30));
        grid.setStyle("-fx-background-color: white;");
        return grid;
    }
    
    private Label createModernLabel(String text) {
        Label label = new Label(text);
        label.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 600; " +
            "-fx-text-fill: #374151;"
        );
        return label;
    }
    
    private TextField createModernTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setPrefWidth(320);
        textField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #1f2937; " +
            "-fx-prompt-text-fill: #9ca3af; " +
            "-fx-background-color: #f9fafb; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #e5e7eb; " +
            "-fx-border-radius: 10; " +
            "-fx-border-width: 2; " +
            "-fx-padding: 12 16;"
        );
        
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle(
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: #1f2937; " +
                    "-fx-prompt-text-fill: #9ca3af; " +
                    "-fx-background-color: white; " +
                    "-fx-background-radius: 10; " +
                    "-fx-border-color: #667eea; " +
                    "-fx-border-radius: 10; " +
                    "-fx-border-width: 2; " +
                    "-fx-padding: 12 16; " +
                    "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.2), 10, 0, 0, 0);"
                );
            } else {
                textField.setStyle(
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: #1f2937; " +
                    "-fx-prompt-text-fill: #9ca3af; " +
                    "-fx-background-color: #f9fafb; " +
                    "-fx-background-radius: 10; " +
                    "-fx-border-color: #e5e7eb; " +
                    "-fx-border-radius: 10; " +
                    "-fx-border-width: 2; " +
                    "-fx-padding: 12 16;"
                );
            }
        });
        
        return textField;
    }
    
    private void styleModernDialog(DialogPane dialogPane, ButtonType okButtonType) {
        dialogPane.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 30, 0, 0, 10);"
        );
        
        Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle(
                "-fx-font-size: 20px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: #1f2937; " +
                "-fx-padding: 10 0;"
            );
        }
        
        javafx.scene.Node okButton = dialogPane.lookupButton(okButtonType);
        if (okButton != null) {
            okButton.setStyle(
                "-fx-text-fill: white; " +
                "-fx-font-weight: 700; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10 24; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.4), 8, 0, 0, 2);"
            );
        }
        
        javafx.scene.Node cancelButton = dialogPane.lookupButton(ButtonType.CANCEL);
        if (cancelButton != null) {
            cancelButton.setStyle(
                "-fx-background-color: #f3f4f6; " +
                "-fx-text-fill: #6b7280; " +
                "-fx-font-weight: 600; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10 24; " +
                "-fx-cursor: hand;"
            );
        }
    }
    
    private void styleModernAlert(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16;"
        );
        
        Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle(
                "-fx-font-size: 18px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: #1f2937;"
            );
        }
        
        Label contentLabel = (Label) dialogPane.lookup(".content");
        if (contentLabel != null) {
            contentLabel.setStyle(
                "-fx-font-size: 14px; " +
                "-fx-text-fill: #6b7280;"
            );
        }
    }
    
    private void showModernError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("❌ " + title);
        alert.setContentText(content);
        styleModernAlert(alert);
        alert.showAndWait();
    }
    
    private void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("✅ " + title);
        alert.setContentText(content);
        styleModernAlert(alert);
        alert.showAndWait();
    }
}