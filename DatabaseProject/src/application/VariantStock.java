package application;

public class VariantStock {
    private final int variantId;
    private final String color;
    private final String size;
    private final String material;
    private final int stock;

    public VariantStock(int variantId, String color, String size, String material, int stock) {
        this.variantId = variantId;
        this.color = color;
        this.size = size;
        this.material = material;
        this.stock = stock;
    }
    public int getVariantId() { return variantId; }
    public String getColor() { return color; }
    public String getSize() { return size; }
    public String getMaterial() { return material; }
    public int getStock() { return stock; }
}