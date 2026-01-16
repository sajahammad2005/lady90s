package application;

public class ProductSummary {
    private final int productId;
    private final String name;
    private final String category;
    private final int totalStock;

    public ProductSummary(int productId, String name, String category, int totalStock) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.totalStock = totalStock;
    }
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getTotalStock() { return totalStock; }
}