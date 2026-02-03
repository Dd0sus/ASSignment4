package model;

public abstract class Staff {
    protected int id;
    protected String name;
    protected double salary;
    protected String role;

    public Staff(int id, String name, double salary, String role) {
        setId(id);
        setName(name);
        setSalary(salary);
        this.role = role;
    }

    public abstract void work();

    public int getId() { return id; }
    public String getName() { return name; }
    public double getSalary() { return salary; }
    public String getRole() { return role; }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive!!!!!!!!!!!!");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty!!!!!!!!!!!!");
        }
        this.name = name;
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative!!!!!!!!!!");
        }
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Staff{id=" + id + ", name='" + name + "', salary=" + salary + ", role='" + role + "'}";
    }
}