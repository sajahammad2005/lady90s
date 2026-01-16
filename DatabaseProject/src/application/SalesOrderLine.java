package application;

public class SalesOrderLine {

    private int sales_order_line_id;
    private int sales_order_id;
    private int product_variant_id;
    private int quantity;
    private double unit_price;

    public SalesOrderLine() {
    }

    public SalesOrderLine(int sales_order_line_id, int sales_order_id,
                          int product_variant_id, int quantity, double unit_price) {
        this.sales_order_line_id = sales_order_line_id;
        this.sales_order_id = sales_order_id;
        this.product_variant_id = product_variant_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    public int getSales_order_line_id() {
        return sales_order_line_id;
    }

    public void setSales_order_line_id(int sales_order_line_id) {
        this.sales_order_line_id = sales_order_line_id;
    }

    public int getSales_order_id() {
        return sales_order_id;
    }

    public void setSales_order_id(int sales_order_id) {
        this.sales_order_id = sales_order_id;
    }

    public int getProduct_variant_id() {
        return product_variant_id;
    }

    public void setProduct_variant_id(int product_variant_id) {
        this.product_variant_id = product_variant_id;
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
