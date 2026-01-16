package application;

import java.sql.Date;

public class PurchaseOrder {

    private int purchase_order_id;
    private int supplier_id;
    private int staff_id;
    private Date order_date;
    private double total_amount;
    private String status;

    // 🔹 Constructor فارغ
    public PurchaseOrder() {
    }

    // 🔹 Constructor كامل
    public PurchaseOrder(int purchase_order_id, int supplier_id, int staff_id,
                         Date order_date, double total_amount, String status) {
        this.purchase_order_id = purchase_order_id;
        this.supplier_id = supplier_id;
        this.staff_id = staff_id;
        this.order_date = order_date;
        this.total_amount = total_amount;
        this.status = status;
    }

    // 🔹 Getters & Setters
    public int getPurchase_order_id() {
        return purchase_order_id;
    }

    public void setPurchase_order_id(int purchase_order_id) {
        this.purchase_order_id = purchase_order_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
