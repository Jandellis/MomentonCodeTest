package com.momenton;

/**
 * Created by james_000 on 29/10/2017.
 */
public class Employee {
    private String employeeName;
    private long id;
    private long managerId;

    public Employee(String employeeName, long id, long managerId) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public long getManagerId() {
        return managerId;
    }

    public void setManagerId(long managerId) {
        this.managerId = managerId;
    }
}
