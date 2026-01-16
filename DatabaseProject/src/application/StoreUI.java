package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

public class StoreUI {

    private final Stage stage;
    private final Connection conn;
    private final StoreDAO dao;

    public StoreUI(Stage stage, Connection conn) {
        this.stage = stage;
        this.conn = conn;
        this.dao = new StoreDAO(conn);
    }

    public Scene createScene() {
        stage.setTitle("Lady90s - Store");
        stage.setWidth(1100);
        stage.setHeight(650);

        return buildCategoriesScene();
    }


    private Scene buildCategoriesScene() {
        Label title = new Label("The Products");
        title.setFont(Font.font(26));

        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        BorderPane root = new BorderPane();
        root.setTop(wrapTop(title));
        root.setCenter(scroll);

        try {
            List<CategoryCount> categories = dao.getCategoriesWithCounts();

            int col = 0, row = 0;
            int maxCols = 5;

            for (CategoryCount c : categories) {
                Pane card = categoryCard(c.getCategory(), c.getCount(), () -> {
                    Scene productsScene = buildProductsScene(c.getCategory());
                    stage.setScene(productsScene);
                });

                grid.add(card, col, row);

                col++;
                if (col == maxCols) { col = 0; row++; }
            }
        } catch (Exception e) {
            root.setCenter(new Label("Error loading categories: " + e.getMessage()));
        }

        return new Scene(root);
    }

    private Scene buildProductsScene(String category) {
        Button back = new Button("⬅ رجوع");
        back.setOnAction(e -> stage.setScene(buildCategoriesScene()));

        Label title = new Label(category);
        title.setFont(Font.font(24));

        HBox header = new HBox(12, back, title);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));

        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(scroll);

        try {
            List<ProductSummary> products = dao.getProductsByCategory(category);

            int col = 0, row = 0;
            int maxCols = 4;

            for (ProductSummary p : products) {
                Pane card = productCard(p, () -> showVariantsPopup(p));
                grid.add(card, col, row);

                col++;
                if (col == maxCols) { col = 0; row++; }
            }
        } catch (Exception e) {
            root.setCenter(new Label("Error loading products: " + e.getMessage()));
        }

        return new Scene(root);
    }

    private void showVariantsPopup(ProductSummary product) {
        Stage pop = new Stage();
        pop.setTitle("Variants - " + product.getName());

        TableView<VariantStock> table = new TableView<>();

        TableColumn<VariantStock, Number> c1 = new TableColumn<>("Variant ID");
        c1.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getVariantId()));

        TableColumn<VariantStock, String> c2 = new TableColumn<>("Color");
        c2.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getColor()));

        TableColumn<VariantStock, String> c3 = new TableColumn<>("Size");
        c3.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getSize()));

        TableColumn<VariantStock, String> c4 = new TableColumn<>("Material");
        c4.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getMaterial()));

        TableColumn<VariantStock, Number> c5 = new TableColumn<>("Stock");
        c5.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getStock()));

        table.getColumns().addAll(c1, c2, c3, c4, c5);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            List<VariantStock> variants = dao.getVariantsByProduct(product.getProductId());
            table.getItems().setAll(variants);
        } catch (Exception e) {
            table.setPlaceholder(new Label("Error: " + e.getMessage()));
        }

        VBox root = new VBox(10,
                new Label("Product: " + product.getName()),
                new Label("Total stock: " + product.getTotalStock()),
                table
        );
        root.setPadding(new Insets(15));

        pop.setScene(new Scene(root, 700, 450));
        pop.show();
    }

    // ===================== UI HELPERS =====================

    private HBox wrapTop(Label title) {
        HBox top = new HBox(title);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(18));
        return top;
    }

    private Pane categoryCard(String categoryName, int badgeNumber, Runnable onClick) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(14));
        card.setPrefSize(170, 120);
        card.setAlignment(Pos.CENTER);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 14;
            -fx-border-radius: 14;
            -fx-border-color: #e6e6e6;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0.1, 0, 3);
        """);

        Label badge = new Label(String.valueOf(badgeNumber));
        badge.setStyle("""
            -fx-background-color: black;
            -fx-text-fill: white;
            -fx-padding: 3 8 3 8;
            -fx-background-radius: 999;
            -fx-font-size: 12;
        """);

        StackPane badgeWrap = new StackPane(badge);
        badgeWrap.setAlignment(Pos.TOP_LEFT);

        Label name = new Label(categoryName);
        name.setFont(Font.font(15));

        Label hint = new Label("اضغطي للعرض");
        hint.setStyle("-fx-text-fill: #777; -fx-font-size: 12;");

        card.getChildren().addAll(badgeWrap, name, hint);

        card.setOnMouseClicked(e -> onClick.run());
        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-border-color: #cfcfcf;"));
        return card;
    }

    private Pane productCard(ProductSummary p, Runnable onClick) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(14));
        card.setPrefSize(230, 130);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 14;
            -fx-border-radius: 14;
            -fx-border-color: #e6e6e6;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0.1, 0, 3);
        """);

        Label name = new Label(p.getName());
        name.setFont(Font.font(16));

        Label stock = new Label("Stock: " + p.getTotalStock());
        stock.setStyle("-fx-text-fill: #444; -fx-font-size: 13;");

        Label open = new Label("Variants");
        open.setStyle("-fx-text-fill: #777; -fx-font-size: 12;");

        card.getChildren().addAll(name, stock, open);
        card.setOnMouseClicked(e -> onClick.run());
        return card;
    }
}
