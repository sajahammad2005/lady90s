package application;

import java.sql.Date;

public class SalesOrder {

    private int sales_order_id;
    private int customer_id;
    private int staff_id;
    private Date order_date;
    private double total_amount;
    private String status;

    // 🔹 Constructor فارغ
    public SalesOrder() {
    }

    // 🔹 Constructor كامل
    public SalesOrder(int sales_order_id, int customer_id, int staff_id,
                      Date order_date, double total_amount, String status) {
        this.sales_order_id = sales_order_id;
        this.customer_id = customer_id;
        this.staff_id = staff_id;
        this.order_date = order_date;
        this.total_amount = total_amount;
        this.status = status;
    }

    // 🔹 Getters & Setters
    public int getSales_order_id() {
        return sales_order_id;
    }

    public void setSales_order_id(int sales_order_id) {
        this.sales_order_id = sales_order_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
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
