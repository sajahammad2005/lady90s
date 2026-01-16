package application;

import java.sql.Date;

public class Staff {

    private int staff_id;
    private int branch_id;
    private int department_id;
    private String name;
    private String position;
    private double salary;
    private Date hire_date;

    // 🔹 Constructor فارغ
    public Staff() {
    }

    // 🔹 Constructor كامل
    public Staff(int staff_id, int branch_id, int department_id,
                 String name, String position, double salary, Date hire_date) {
        this.staff_id = staff_id;
        this.branch_id = branch_id;
        this.department_id = department_id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.hire_date = hire_date;
    }

    // 🔹 Getters & Setters
    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getHire_date() {
        return hire_date;
    }

    public void setHire_date(Date hire_date) {
        this.hire_date = hire_date;
    }
}

