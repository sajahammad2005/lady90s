package application;

public class VariantModel {

    private int variantId;
    private String color;
    private String size;
    private String material;
    private double additionalPrice;
    private int stock;

    public VariantModel(int variantId, String color, String size, String material,
                        double additionalPrice, int stock) {
        this.variantId = variantId;
        this.color = color;
        this.size = size;
        this.material = material;
        this.additionalPrice = additionalPrice;
        this.stock = stock;
    }

    public int getVariantId() {
        return variantId;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getMaterial() {
        return material;
    }

    public double getAdditionalPrice() {
        return additionalPrice;
    }

    // ✅ هذا الايرور اللي عندك (getStock undefined)
    public int getStock() {
        return stock;
    }
}