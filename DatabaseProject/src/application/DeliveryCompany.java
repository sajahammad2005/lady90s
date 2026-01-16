package application;

public class DeliveryCompany {

    private int delivery_id;
    private String name;
    private String phone;
    private double delivery_fee;

    // 🔹 Constructor فارغ
    public DeliveryCompany() {
    }

    // 🔹 Constructor كامل
    public DeliveryCompany(int delivery_id, String name, String phone, double delivery_fee) {
        this.delivery_id = delivery_id;
        this.name = name;
        this.phone = phone;
        this.delivery_fee = delivery_fee;
    }

    // 🔹 Getters & Setters
    public int getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(double delivery_fee) {
        this.delivery_fee = delivery_fee;
    }
}
