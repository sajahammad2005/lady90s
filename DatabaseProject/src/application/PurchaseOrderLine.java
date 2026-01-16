package application;

public class PurchaseOrderLine {

    private int purchase_order_line_id;
    private int purchase_order_id;
    private int product_id;
    private int quantity;
    private double unit_price;

    // 🔹 Constructor فارغ
    public PurchaseOrderLine() {
    }

    // 🔹 Constructor كامل
    public PurchaseOrderLine(int purchase_order_line_id, int purchase_order_id,
                             int product_id, int quantity, double unit_price) {
        this.purchase_order_line_id = purchase_order_line_id;
        this.purchase_order_id = purchase_order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    // 🔹 Getters & Setters
    public int getPurchase_order_line_id() {
        return purchase_order_line_id;
    }

    public void setPurchase_order_line_id(int purchase_order_line_id) {
        this.purchase_order_line_id = purchase_order_line_id;
    }

    public int getPurchase_order_id() {
        return purchase_order_id;
    }

    public void setPurchase_order_id(int purchase_order_id) {
        this.purchase_order_id = purchase_order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }
}
