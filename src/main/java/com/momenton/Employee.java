package com.momenton;

/**
 * Created by james_000 on 29/10/2017.
 */
public class Employee {
    private String employeeName;
    private Long id;
    private Long managerId;

    public Employee(String employeeName, Long id, Long managerId) {
        this.employeeName = employeeName;
        this.id = id;
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeName='" + employeeName + '\'' +
                ", id=" + id +
                ", managerId=" + managerId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (employeeName != null ? !employeeName.equals(employee.employeeName) : employee.employeeName != null)
            return false;
        if (id != null ? !id.equals(employee.id) : employee.id != null) return false;
        return managerId != null ? managerId.equals(employee.managerId) : employee.managerId == null;
    }

    @Override
    public int hashCode() {
        int result = employeeName != null ? employeeName.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (managerId != null ? managerId.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
