package application;

import java.sql.*;
import java.util.*;

public class StoreDAO {
    private final Connection conn;

    public StoreDAO(Connection conn) {
        this.conn = conn;
    }

    public List<CategoryCount> getCategoriesWithCounts() throws SQLException {
        String sql = """
            SELECT p.category, COUNT(DISTINCT p.product_id) AS products_count
            FROM Product p
            JOIN ProductVariant pv ON pv.product_id = p.product_id
            JOIN Inventory i ON i.variant_id = pv.variant_id
            WHERE i.quantity > 0
            GROUP BY p.category
            ORDER BY p.category
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<CategoryCount> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new CategoryCount(
                        rs.getString("category"),
                        rs.getInt("products_count")
                ));
            }
            return list;
        }
    }

    public List<ProductSummary> getProductsByCategory(String category) throws SQLException {
        String sql = """
            SELECT p.product_id, p.name AS product_name, p.category,
                   SUM(i.quantity) AS total_stock
            FROM Product p
            JOIN ProductVariant pv ON pv.product_id = p.product_id
            JOIN Inventory i ON i.variant_id = pv.variant_id
            WHERE p.category = ?
              AND i.quantity > 0
            GROUP BY p.product_id, p.name, p.category
            ORDER BY p.name
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                List<ProductSummary> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new ProductSummary(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getString("category"),
                            rs.getInt("total_stock")
                    ));
                }
                return list;
            }
        }
    }

    public List<VariantStock> getVariantsByProduct(int productId) throws SQLException {
        String sql = """
            SELECT pv.variant_id, pv.color, pv.size, pv.material,
                   SUM(i.quantity) AS variant_total_stock
            FROM ProductVariant pv
            JOIN Inventory i ON i.variant_id = pv.variant_id
            WHERE pv.product_id = ?
              AND i.quantity > 0
            GROUP BY pv.variant_id, pv.color, pv.size, pv.material
            ORDER BY pv.variant_id
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                List<VariantStock> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new VariantStock(
                            rs.getInt("variant_id"),
                            rs.getString("color"),
                            rs.getString("size"),
                            rs.getString("material"),
                            rs.getInt("variant_total_stock")
                    ));
                }
                return list;
            }
        }
    }
}
