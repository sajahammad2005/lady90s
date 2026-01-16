package application;

import java.sql.Date;

public class Payment {

    private int payment_id;
    private int sales_order_id;
    private Date payment_date;
    private double amount;
    private String payment_method;
    private String status;

    public Payment() {
    }

    public Payment(int payment_id, int sales_order_id, Date payment_date,
                   double amount, String payment_method, String status) {
        this.payment_id = payment_id;
        this.sales_order_id = sales_order_id;
        this.payment_date = payment_date;
        this.amount = amount;
        this.payment_method = payment_method;
        this.status = status;
    }

    // 🔹 Getters & Setters
    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getSales_order_id() {
        return sales_order_id;
    }

    public void setSales_order_id(int sales_order_id) {
        this.sales_order_id = sales_order_id;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

