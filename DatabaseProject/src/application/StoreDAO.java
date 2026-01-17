package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {

    private final Connection conn;

    public StoreDAO(Connection conn) {
        this.conn = conn;
    }

    public List<ProductCardModel> getAllProductsForCards() throws SQLException {
        List<ProductCardModel> list = new ArrayList<>();

        String sql = """
            SELECT 
                p.product_id,
                p.name,
                p.description,
                p.base_price,
                COALESCE(MIN(pv.additional_price), 0) AS min_add,
                COALESCE(SUM(i.quantity), 0) AS total_stock
            FROM product p
            LEFT JOIN productvariant pv ON pv.product_id = p.product_id
            LEFT JOIN inventory i ON i.variant_id = pv.variant_id
            GROUP BY p.product_id, p.name, p.description, p.base_price
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

                list.add(new ProductCardModel(productId, name, desc, null, basePrice, minAdd, stock));
            }
        }

        return list;
    }

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

    public List<VariantModel> getVariantsForProduct(int productId) throws SQLException {
        return getVariantsByProduct(productId);
    }

   
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
    

    public List<ProductCardModel> getProductsByCategory(String category) throws SQLException {
        List<ProductCardModel> list = new ArrayList<>();

        String sql = """
            SELECT 
                p.product_id,
                p.name,
                p.description,
                p.base_price,
                COALESCE(MIN(pv.additional_price), 0) AS min_add,
                COALESCE(SUM(i.quantity), 0) AS total_stock
            FROM product p
            LEFT JOIN productvariant pv ON pv.product_id = p.product_id
            LEFT JOIN inventory i ON i.variant_id = pv.variant_id
            WHERE p.category = ?
            GROUP BY p.product_id, p.name, p.description, p.base_price
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

                    list.add(new ProductCardModel(productId, name, desc, null, basePrice, minAdd, stock));
                }
            }
        }

        return list;
    }
}