package application;

public class ProductVariant {

    private int variant_id;
    private int product_id;
    private String color;
    private String size;
    private String material;
    private double additional_price;

    // 🔹 Constructor فارغ
    public ProductVariant() {
    }

    // 🔹 Constructor كامل
    public ProductVariant(int variant_id, int product_id, String color,
                          String size, String material, double additional_price) {
        this.variant_id = variant_id;
        this.product_id = product_id;
        this.color = color;
        this.size = size;
        this.material = material;
        this.additional_price = additional_price;
    }

    // 🔹 Getters & Setters
    public int getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(int variant_id) {
        this.variant_id = variant_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double getAdditional_price() {
        return additional_price;
    }

    public void setAdditional_price(double additional_price) {
        this.additional_price = additional_price;
    }
}
