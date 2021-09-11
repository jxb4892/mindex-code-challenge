package com.mindex.challenge.data;

import java.lang.Math.*;
import java.util.Calendar;

public class Compensation {
    private Employee employee;
    private double salary;
    private Calendar effectiveDate;

    public Compensation() {

    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public double getSalary() {
        return Math.floor(this.salary * 100) / 100;
    }

    public void setSalary(double salary) {
        this.salary = Math.floor(salary * 100) / 100;
    }

    public Calendar getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(Calendar effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

}