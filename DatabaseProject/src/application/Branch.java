package application;

public class Branch {

    private int branch_id;
    private String name;
    private String city;
    private String address;
    private String phone;

    public Branch() {
    }

    public Branch(int branch_id, String name, String city,
                  String address, String phone) {
        this.branch_id = branch_id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.phone = phone;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
