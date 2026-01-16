package application;

import java.sql.Date;

public class Shipment {

    private int shipment_id;
    private int sales_order_id;
    private int delivery_id;
    private Date shipment_date;
    private Date delivery_date;
    private String shipping_address;
    private String status;
    private double customer_paid_total;
    private double amount_transferred_to_store;

    // 🔹 Constructor فارغ
    public Shipment() {
    }

    // 🔹 Constructor كامل
    public Shipment(int shipment_id, int sales_order_id, int delivery_id,
                    Date shipment_date, Date delivery_date,
                    String shipping_address, String status,
                    double customer_paid_total, double amount_transferred_to_store) {
        this.shipment_id = shipment_id;
        this.sales_order_id = sales_order_id;
        this.delivery_id = delivery_id;
        this.shipment_date = shipment_date;
        this.delivery_date = delivery_date;
        this.shipping_address = shipping_address;
        this.status = status;
        this.customer_paid_total = customer_paid_total;
        this.amount_transferred_to_store = amount_transferred_to_store;
    }

    // 🔹 Getters & Setters
    public int getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(int shipment_id) {
        this.shipment_id = shipment_id;
    }

    public int getSales_order_id() {
        return sales_order_id;
    }

    public void setSales_order_id(int sales_order_id) {
        this.sales_order_id = sales_order_id;
    }

    public int getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        this.delivery_id = delivery_id;
    }

    public Date getShipment_date() {
        return shipment_date;
    }

    public void setShipment_date(Date shipment_date) {
        this.shipment_date = shipment_date;
    }

    public Date getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(Date delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCustomer_paid_total() {
        return customer_paid_total;
    }

    public void setCustomer_paid_total(double customer_paid_total) {
        this.customer_paid_total = customer_paid_total;
    }

    public double getAmount_transferred_to_store() {
        return amount_transferred_to_store;
    }

    public void setAmount_transferred_to_store(double amount_transferred_to_store) {
        this.amount_transferred_to_store = amount_transferred_to_store;
    }
}
