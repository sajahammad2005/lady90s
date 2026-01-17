package application;

public class Product {

    private int productId;        
    private String name;          
    private String category;      
    private String brand;         
    private double basePrice;     
    private String description; 
    private String imageKey;

    public Product() {
    }

    public Product(int productId, String name, String category, String brand, double basePrice, String description) {
        setProductId(productId);
        setName(name);
        setCategory(category);
        setBrand(brand);
        setBasePrice(basePrice);
        setDescription(description);
    }
    public Product(int productId, String name, String category, String brand, double basePrice, String description,String imageKey) {
      this(productId, name,  category,  brand,  basePrice,  description);
        setImageKey(imageKey);
    }
    //ds

    public String getImageKey() {
		return imageKey;
	}

    public void setImageKey(String imageKey) {
        // عادي يكون null بالبداية
        if (imageKey != null) imageKey = imageKey.trim();
        this.imageKey = (imageKey != null && !imageKey.isEmpty()) ? imageKey : null;
    }

	public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        if (productId <= 0) {
            throw new IllegalArgumentException("productId must be > 0");
        }
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null/empty");
        }
        this.name = name.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("category cannot be null/empty");
        }
        this.category = category.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("brand cannot be null/empty");
        }
        this.brand = brand.trim();
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("basePrice cannot be negative");
        }
        this.basePrice = basePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description cannot be null");
        }
        this.description = description.trim();
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", basePrice=" + basePrice +
                ", description='" + description + '\'' +
                '}';
    }
}