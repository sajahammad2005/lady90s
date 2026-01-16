package application;

public class Supplier {

    private int supplier_id;
    private String name;
    private String email;
    private String phone;
    private String address;

    public Supplier() {
    }

    public Supplier(int supplier_id, String name, String email, String phone, String address) {
        this.supplier_id = supplier_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

