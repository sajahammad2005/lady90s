package application;

public class Warehouse {

    private int warehouseId;  
    private String name;       
    private Integer branchId;  
    private String location;       

    public Warehouse() {
    }

    public Warehouse(int warehouseId, String name) {
        setWarehouseId(warehouseId);
        setName(name);
    }

    public Warehouse(int warehouseId, String name, Integer branchId, String location) {
        setWarehouseId(warehouseId);
        setName(name);
        setBranchId(branchId);
        setCity(location);
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        if (warehouseId <= 0) {
            throw new IllegalArgumentException("warehouseId must be > 0");
        }
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null/empty");
        }
        this.name = name.trim();
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        if (branchId != null && branchId <= 0) {
            throw new IllegalArgumentException("branchId must be > 0 if provided");
        }
        this.branchId = branchId;
    }

    public String getCity() {
        return location;
    }

    public void setCity(String city) {
        if (city == null) {
            this.location = null;
            return;
        }
        this.location = city.trim();
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "warehouseId=" + warehouseId +
                ", name='" + name + '\'' +
                ", branchId=" + branchId +
                ", city='" + location + '\'' +
                '}';
    }
}