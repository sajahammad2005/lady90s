package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
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

  
    private HBox createTopBar() {

        ImageView logo = new ImageView(loadImageSafe("/images/logo.jpeg")); 
        logo.setFitHeight(34);
        logo.setFitWidth(34);
        logo.setPreserveRatio(true);

        Button logoBtn = new Button();
        logoBtn.setGraphic(logo);
        logoBtn.setStyle("""
            -fx-background-color: transparent;
            -fx-padding: 4;
        """);
        logoBtn.setOnAction(e -> stage.setScene(buildCategoriesScene()));

        Button searchBtn = iconBtn("🔍");
        searchBtn.setOnAction(e -> Main.validAlert("Search", "لسا بنعمل واجهة السيرتش"));

        Button accountBtn = iconBtn("👤");
        accountBtn.setOnAction(e -> Main.validAlert("Account", "لسا بنعمل واجهة الحساب"));

        Button wishBtn = iconBtn("♡");
        wishBtn.setOnAction(e -> Main.validAlert("Wishlist", "لسا بنعمل واجهة الويش ليست"));

        Button cartBtn = iconBtn("🛒");
        cartBtn.setOnAction(e -> Main.validAlert("Cart", "لسا بنعمل واجهة السلة"));

        Label wishCount = smallBadge("0");
        Label cartCount = smallBadge("0");

        StackPane wishWrap = new StackPane(wishBtn, wishCount);
        StackPane.setAlignment(wishCount, Pos.TOP_RIGHT);

        StackPane cartWrap = new StackPane(cartBtn, cartCount);
        StackPane.setAlignment(cartCount, Pos.TOP_RIGHT);

        HBox left = new HBox(10, searchBtn, accountBtn, wishWrap, cartWrap);
        left.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox right = new HBox(8, logoBtn);
        right.setAlignment(Pos.CENTER_RIGHT);

        HBox bar = new HBox(15, left, spacer, right);
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(10, 14, 10, 14));
        bar.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #eeeeee;
            -fx-border-width: 0 0 1 0;
        """);

        return bar;
    }

    private Button iconBtn(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 16;
            -fx-padding: 6 8 6 8;
        """);
        return b;
    }

    private Label smallBadge(String txt) {
        Label l = new Label(txt);
        l.setStyle("""
            -fx-background-color: black;
            -fx-text-fill: white;
            -fx-font-size: 10;
            -fx-padding: 2 6 2 6;
            -fx-background-radius: 999;
        """);
        l.setTranslateX(6);
        l.setTranslateY(-6);
        return l;
    }

    private BorderPane wrapWithTopBar(javafx.scene.Node centerNode) {
        BorderPane root = new BorderPane();
        root.setTop(createTopBar());         
        root.setCenter(centerNode);
        root.setStyle("-fx-background-color: #fafafa;");
        return root;
    }


    public Scene buildCategoriesScene() {
        Label title = new Label("أصنافنا");
        title.setFont(Font.font(26));

        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox page = new VBox(10, wrapTop(title), scroll);
        page.setPadding(new Insets(8, 0, 0, 0));

        BorderPane root = wrapWithTopBar(page);

        try {
            List<CategoryCount> categories = dao.getCategoriesWithCounts();

            int col = 0, row = 0;
            int maxCols = 4;

            Pane allBags = categoryCard("كل الشناتي", -1, "/images/Dior.jpeg", () -> {
                stage.setScene(buildAllProductsScene());
            });
            grid.add(allBags, col, row);
            col++;
            if (col == maxCols) { col = 0; row++; }

            for (CategoryCount c : categories) {
                Pane card = categoryCard(c.getCategory(), c.getCount(), null, () -> {
                    stage.setScene(buildProductsByCategoryScene(c.getCategory()));
                });

                grid.add(card, col, row);

                col++;
                if (col == maxCols) { col = 0; row++; }
            }

            if (categories.isEmpty()) {
                root.setCenter(new Label("No categories found."));
            }

        } catch (Exception e) {
            root.setCenter(new Label("Error loading categories: " + e.getMessage()));
        }

        return new Scene(root);
    }

    private Scene buildProductsByCategoryScene(String category) {
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
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox page = new VBox(header, scroll);
        BorderPane root = wrapWithTopBar(page);

        try {
            List<ProductCardModel> products = dao.getProductsByCategory(category);

            int col = 0, row = 0;
            int maxCols = 4;

            for (ProductCardModel p : products) {
                Pane card = productCard(p);
                grid.add(card, col, row);

                col++;
                if (col == maxCols) { col = 0; row++; }
            }

            if (products.isEmpty()) {
                root.setCenter(new Label("No products found in this category."));
            }

        } catch (Exception e) {
            root.setCenter(new Label("Error loading products: " + e.getMessage()));
        }

        return new Scene(root);
    }

    private Pane categoryCard(String categoryName, int badgeNumber, String imagePath, Runnable onClick) {

        ImageView img = new ImageView(loadImageSafe(imagePath));
        img.setFitWidth(120);
        img.setFitHeight(120);
        img.setPreserveRatio(true);

        StackPane imgWrap = new StackPane(img);
        imgWrap.setPrefSize(140, 140);
        imgWrap.setStyle("""
            -fx-background-color: #f2f2f2;
            -fx-background-radius: 999;
        """);

        Label badge = new Label(badgeNumber < 0 ? "ALL" : String.valueOf(badgeNumber));
        badge.setStyle("""
            -fx-background-color: black;
            -fx-text-fill: white;
            -fx-padding: 4 9 4 9;
            -fx-background-radius: 999;
            -fx-font-size: 12;
        """);

        StackPane badgeWrap = new StackPane(badge);
        badgeWrap.setAlignment(Pos.TOP_LEFT);
        badgeWrap.setPadding(new Insets(8));

        StackPane top = new StackPane(imgWrap, badgeWrap);
        top.setAlignment(Pos.TOP_LEFT);

        Label name = new Label(categoryName);
        name.setFont(Font.font(15));
        name.setStyle("-fx-font-weight: bold;");

        VBox card = new VBox(10, top, name);
        card.setPadding(new Insets(14));
        card.setPrefSize(230, 230);
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 16;
            -fx-border-radius: 16;
            -fx-border-color: #e6e6e6;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0.1, 0, 3);
        """);

        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-border-color: #cfcfcf;"));
        card.setOnMouseClicked(e -> onClick.run());

        return card;
    }


    public Scene buildAllProductsScene() {
        Label title = new Label("المنتجات");
        title.setFont(Font.font(26));

        Button back = new Button("⬅ الأصناف");
        back.setOnAction(e -> stage.setScene(buildCategoriesScene()));

        HBox topBar = new HBox(12, back, title);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(18));

        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox page = new VBox(topBar, scroll);
        BorderPane root = wrapWithTopBar(page);

        try {
            List<ProductCardModel> products = dao.getAllProductsForCards();

            int col = 0, row = 0;
            int maxCols = 4;

            for (ProductCardModel p : products) {
                Pane card = productCard(p);
                grid.add(card, col, row);

                col++;
                if (col == maxCols) { col = 0; row++; }
            }

            if (products.isEmpty()) {
                root.setCenter(new Label("No products found."));
            }

        } catch (Exception e) {
            root.setCenter(new Label("Error loading products: " + e.getMessage()));
        }

        return new Scene(root);
    }


    private Scene buildProductDetailsScene(ProductCardModel p) {
        Button back = new Button("⬅ رجوع");
        back.setOnAction(e -> stage.setScene(buildAllProductsScene()));

        Label title = new Label(p.getName());
        title.setFont(Font.font(22));

        HBox header = new HBox(12, back, title);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));

        ImageView bigImage = new ImageView(loadImageSafe(p.getImageUrl()));
        bigImage.setFitWidth(420);
        bigImage.setFitHeight(420);
        bigImage.setPreserveRatio(true);
        bigImage.setSmooth(true);

        StackPane imageWrap = new StackPane(bigImage);
        imageWrap.setPadding(new Insets(10));
        imageWrap.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 18;
            -fx-border-radius: 18;
            -fx-border-color: #e6e6e6;
        """);

        Label price = new Label(String.format("%.2f شيكل", p.getDisplayPrice()));
        price.setFont(Font.font(22));
        price.setStyle("-fx-text-fill: #0b6; -fx-font-weight: bold;");

        Label descTitle = new Label("الوصف");
        descTitle.setFont(Font.font(16));
        descTitle.setStyle("-fx-font-weight: bold;");

        Label desc = new Label(p.getDescription() == null ? "" : p.getDescription());
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: #444; -fx-font-size: 14;");

        VBox variantsBox = new VBox(10);
        variantsBox.setPadding(new Insets(10, 0, 0, 0));

        try {
            List<VariantModel> vars = dao.getVariantsByProduct(p.getProductId());

            FlowPane chips = new FlowPane(10, 10);
            chips.setPadding(new Insets(10));
            chips.setPrefWrapLength(520);

            for (VariantModel v : vars) {
                chips.getChildren().add(variantChip(v));
            }

            Label vTitle = new Label("الفاريانتس");
            vTitle.setFont(Font.font(16));
            vTitle.setStyle("-fx-font-weight: bold;");

            variantsBox.getChildren().addAll(vTitle, chips);

        } catch (Exception ex) {
            variantsBox.getChildren().add(new Label("Error loading variants: " + ex.getMessage()));
        }

        Button addToCart = new Button("أضف إلى عربة التسوق");
        addToCart.setPrefWidth(320);
        addToCart.setStyle("""
            -fx-background-color: #c9a46a;
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-background-radius: 999;
            -fx-padding: 10 18 10 18;
        """);
        addToCart.setOnAction(e -> Main.validAlert("تم", "انضافت لعربة التسوق (مبدئياً)"));

        Button addToWish = new Button("♡ أضف للأمنيات");
        addToWish.setPrefWidth(220);
        addToWish.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #ddd;
            -fx-text-fill: #333;
            -fx-font-size: 13;
            -fx-background-radius: 999;
            -fx-border-radius: 999;
            -fx-padding: 10 18 10 18;
        """);
        addToWish.setOnAction(e -> Main.validAlert("تم", "انضافت للأمنيات (مبدئياً)"));

        HBox actions = new HBox(12, addToCart, addToWish);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.setPadding(new Insets(10, 0, 0, 0));

        VBox right = new VBox(10, price, descTitle, desc, variantsBox, actions);
        right.setPadding(new Insets(10));
        right.setPrefWidth(550);

        HBox body = new HBox(18, imageWrap, right);
        body.setPadding(new Insets(15));

        VBox page = new VBox(header, body);
        BorderPane root = wrapWithTopBar(page);

        return new Scene(root);
    }

    // ===================== UI HELPERS =====================

    private HBox wrapTop(Label title) {
        HBox top = new HBox(title);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(18));
        return top;
    }

    private Pane productCard(ProductCardModel p) {

        ImageView img = new ImageView(loadImageSafe(p.getImageUrl()));
        img.setFitWidth(230);
        img.setFitHeight(160);
        img.setPreserveRatio(true);
        img.setSmooth(true);

        StackPane imageWrap = new StackPane(img);
        imageWrap.setPrefSize(230, 160);
        imageWrap.setStyle("""
            -fx-background-color: #eee;
            -fx-background-radius: 14;
        """);

        Button wishBtn = circleButton("♡");
        wishBtn.setOnAction(e -> Main.validAlert("Wishlist", "Added to wishlist (temp)."));

        Button cartBtn = circleButton("🛒");
        cartBtn.setOnAction(e -> Main.validAlert("Cart", "Added to cart (temp)."));

        Button viewBtn = circleButton("👁");
        viewBtn.setOnAction(e -> stage.setScene(buildProductDetailsScene(p)));

        HBox overlay = new HBox(10, wishBtn, cartBtn, viewBtn);
        overlay.setAlignment(Pos.CENTER);
        overlay.setVisible(false);

        StackPane imageStack = new StackPane(imageWrap, overlay);

        Label name = new Label(p.getName());
        name.setFont(Font.font(15));
        name.setStyle("-fx-font-weight: bold;");

        Label price = new Label(String.format("%.2f شيكل", p.getDisplayPrice()));
        price.setStyle("-fx-text-fill: #666; -fx-font-size: 13;");

        VBox card = new VBox(10, imageStack, name, price);
        card.setPadding(new Insets(12));
        card.setPrefSize(255, 260);
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 16;
            -fx-border-radius: 16;
            -fx-border-color: #e6e6e6;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0.1, 0, 3);
        """);

        card.setOnMouseEntered(e -> overlay.setVisible(true));
        card.setOnMouseExited(e -> overlay.setVisible(false));
        card.setOnMouseClicked(e -> stage.setScene(buildProductDetailsScene(p)));

        return card;
    }
<<<<<<< HEAD
=======

    private Button circleButton(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: rgba(255,255,255,0.9);
            -fx-background-radius: 999;
            -fx-border-radius: 999;
            -fx-border-color: #ddd;
            -fx-padding: 10 12 10 12;
            -fx-font-size: 14;
        """);
        return b;
    }

    private Pane variantChip(VariantModel v) {
        String txt = safe(v.getColor()) + " | " + safe(v.getSize()) + " | " + safe(v.getMaterial())
                + "  (Stock: " + v.getStock() + ")";

        Label l = new Label(txt);
        l.setStyle("-fx-text-fill: #333; -fx-font-size: 13;");

        HBox box = new HBox(l);
        box.setPadding(new Insets(10));
        box.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 14;
            -fx-border-radius: 14;
            -fx-border-color: #e6e6e6;
        """);

        return box;
    }

    private String safe(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }


    private Image loadImageSafe(String imagePath) {
        try {
            if (imagePath == null || imagePath.isBlank()) {
                return placeholder();
            }

            InputStream is = StoreUI.class.getResourceAsStream(imagePath.startsWith("/") ? imagePath : "/" + imagePath);

            if (is == null) {
                return placeholder();
            }

            return new Image(is);

        } catch (Exception e) {
            return placeholder();
        }
    }

    private Image placeholder() {
        try {
            InputStream is = StoreUI.class.getResourceAsStream("/Dior.jpeg");
            if (is != null) return new Image(is);
        } catch (Exception ignored) {}

        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQIW2P4//8/AwAI/AL+X2vL3wAAAABJRU5ErkJggg==");
    }


    private void showVariantsPopup(ProductCardModel product) {
        Stage pop = new Stage();
        pop.setTitle("Variants - " + product.getName());

        VBox root = new VBox(10, new Label("Use the details page instead."));
        root.setPadding(new Insets(15));

        pop.setScene(new Scene(root, 400, 200));
        pop.show();
    }
>>>>>>> branch 'main' of https://github.com/sajahammad2005/lady90s
}