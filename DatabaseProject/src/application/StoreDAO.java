package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {

    private final Connection conn;

    public StoreDAO(Connection conn) {
        this.conn = conn;
    }
    public String getRandomImageUrlForCategory(String category) throws SQLException {
        // يرجّع مسار جاهز للـ UI مثل: /images/bag1.jpeg
        String key = getRandomImageKeyForCategory(category);
        if (key == null || key.isBlank()) return null;
        return "/images/" + key.trim() + ".jpeg"; // عدلي الامتداد اذا png
    }

    public String getRandomImageKeyForCategory(String category) throws SQLException {
        String sql = """
            SELECT p.image_key
            FROM product p
            WHERE p.category = ?
              AND p.image_key IS NOT NULL
              AND TRIM(p.image_key) <> ''
            ORDER BY RAND()
            LIMIT 1
        """;

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, category);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return rs.getString("image_key");
            }
        }
        return null;
    }
    //d
    // ===================== 1) ALL PRODUCTS (FOR CARDS) =====================
    public List<ProductCardModel> getAllProductsForCards() throws SQLException {
        List<ProductCardModel> list = new ArrayList<>();

        String sql = """
            SELECT 
                p.product_id,
                p.name,
                p.description,
                p.base_price,
                p.image_key,
                COALESCE(MIN(pv.additional_price), 0) AS min_add,
                COALESCE(SUM(i.quantity), 0) AS total_stock
            FROM product p
            LEFT JOIN productvariant pv ON pv.product_id = p.product_id
            LEFT JOIN inventory i ON i.variant_id = pv.variant_id
            GROUP BY p.product_id, p.name, p.description, p.base_price, p.image_key
            ORDER BY p.product_id DESC
        """;

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                double basePrice = rs.getDouble("base_price");
                double minAdd = rs.getDouble("min_add");
                int stock = rs.getInt("total_stock");

                String imageKey = rs.getString("image_key");
                String imageUrl = buildImageUrl(imageKey);

                list.add(new ProductCardModel(productId, name, desc, imageUrl, basePrice, minAdd, stock));
            }
        }

        return list;
    }

    // ===================== 2) VARIANTS (BY PRODUCT) =====================
    public List<VariantModel> getVariantsByProduct(int productId) throws SQLException {
        List<VariantModel> list = new ArrayList<>();

        String sql = """
            SELECT 
                pv.variant_id,
                pv.color,
                pv.size,
                pv.material,
                pv.additional_price,
                COALESCE(SUM(i.quantity), 0) AS stock
            FROM productvariant pv
            LEFT JOIN inventory i ON i.variant_id = pv.variant_id
            WHERE pv.product_id = ?
            GROUP BY pv.variant_id, pv.color, pv.size, pv.material, pv.additional_price
            ORDER BY pv.variant_id DESC
        """;

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, productId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int variantId = rs.getInt("variant_id");
                    String color = rs.getString("color");
                    String size = rs.getString("size");
                    String material = rs.getString("material");
                    double addPrice = rs.getDouble("additional_price");
                    int stock = rs.getInt("stock");

                    list.add(new VariantModel(variantId, color, size, material, addPrice, stock));
                }
            }
        }

        return list;
    }
    // Alias إذا كودك القديم كان بستدعيها
    public List<VariantModel> getVariantsForProduct(int productId) throws SQLException {
        return getVariantsByProduct(productId);
    }

    // ===================== 3) CATEGORIES + COUNTS =====================
    public List<CategoryCount> getCategoriesWithCounts() throws SQLException {
        List<CategoryCount> list = new ArrayList<>();

        String sql = """
            SELECT 
                p.category AS category,
                COUNT(DISTINCT p.product_id) AS cnt
            FROM product p
            WHERE p.category IS NOT NULL AND TRIM(p.category) <> ''
            GROUP BY p.category
            ORDER BY cnt DESC, p.category
        """;

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                String category = rs.getString("category");
                int count = rs.getInt("cnt");
                list.add(new CategoryCount(category, count));
            }
        }

        return list;
    }

    // ===================== 4) PRODUCTS BY CATEGORY =====================
    public List<ProductCardModel> getProductsByCategory(String category) throws SQLException {
        List<ProductCardModel> list = new ArrayList<>();

        String sql = """
            SELECT 
                p.product_id,
                p.name,
                p.description,
                p.base_price,
                p.image_key,
                COALESCE(MIN(pv.additional_price), 0) AS min_add,
                COALESCE(SUM(i.quantity), 0) AS total_stock
            FROM product p
            LEFT JOIN productvariant pv ON pv.product_id = p.product_id
            LEFT JOIN inventory i ON i.variant_id = pv.variant_id
            WHERE p.category = ?
            GROUP BY p.product_id, p.name, p.description, p.base_price, p.image_key
            ORDER BY p.product_id DESC
        """;

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, category);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    String name = rs.getString("name");
                    String desc = rs.getString("description");
                    double basePrice = rs.getDouble("base_price");
                    double minAdd = rs.getDouble("min_add");
                    int stock = rs.getInt("total_stock");

                    String imageKey = rs.getString("image_key");
                    String imageUrl = buildImageUrl(imageKey);

                    list.add(new ProductCardModel(productId, name, desc, imageUrl, basePrice, minAdd, stock));
                }
            }
        }

        return list;
    }

    // ===================== HELPER: BUILD IMAGE URL FROM image_key =====================
    private String buildImageUrl(String imageKey) {
        if (imageKey == null) return null;
        String key = imageKey.trim();
        if (key.isEmpty()) return null;

        // ✅ صورك عندك داخل package application (حسب الصورة عندك داخل src/application)
        // إذا الامتداد مختلف (png) غيّري ".jpeg" لـ ".png"
        return "/application/" + key + ".jpeg";
    }
}