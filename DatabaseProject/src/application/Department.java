package application;

public class Department {

    private int department_id;
    private String name;
    private String description;

    // 🔹 Constructor فارغ
    public Department() {
    }

    // 🔹 Constructor كامل
    public Department(int department_id, String name, String description) {
        this.department_id = department_id;
        this.name = name;
        this.description = description;
    }

    // 🔹 Getters & Setters
    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
