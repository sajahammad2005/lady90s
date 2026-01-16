package application;

import java.sql.Date;

public class Inventory {

    private int inventory_id;
    private int variant_id;
    private int warehouse_id;
    private int quantity;
    private Date last_updated;

    public Inventory() {
    }

    // 🔹 Constructor كامل
    public Inventory(int inventory_id, int variant_id, int warehouse_id,
                     int quantity, Date last_updated) {
        this.inventory_id = inventory_id;
        this.variant_id = variant_id;
        this.warehouse_id = warehouse_id;
        this.quantity = quantity;
        this.last_updated = last_updated;
    }

    // 🔹 Getters & Setters
    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public int getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(int variant_id) {
        this.variant_id = variant_id;
    }

    public int getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(int warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }
}
