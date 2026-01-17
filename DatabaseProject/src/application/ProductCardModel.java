package application;

public class ProductCardModel {

    private int productId;
    private String name;
    private String description;
    private String imageUrl;   // أو imagePath (خليتها imageUrl عشان انتي مستعملة getImageUrl())
    private double basePrice;
    private double minAdditionalPrice; // أقل additional price بين الفاريانتس (للعرض كبداية)
    private int totalStock;

    public ProductCardModel(int productId, String name, String description,
                            String imageUrl, double basePrice,
                            double minAdditionalPrice, int totalStock) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.basePrice = basePrice;
        this.minAdditionalPrice = minAdditionalPrice;
        this.totalStock = totalStock;
    }

    // ✅ REQUIRED GETTERS (حسب الايرورز اللي عندك)
    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    // انتي مرة استخدمتي getImageUrl() ومرة getImagePath()
    public String getImageUrl() {
        return imageUrl;
    }

    public String getImagePath() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    // السعر اللي بتعرضيه على الكارد/صفحة التفاصيل
    public double getDisplayPrice() {
        return basePrice + (minAdditionalPrice < 0 ? 0 : minAdditionalPrice);
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}