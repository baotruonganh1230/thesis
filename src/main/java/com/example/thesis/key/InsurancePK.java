package com.example.thesis.key;

import com.example.thesis.entity.Employee;
import com.example.thesis.entity.Insurance_Type;

import java.io.Serializable;
import java.util.Objects;

public class InsurancePK implements Serializable {
    private Long id;
    private Employee employee;
    private Insurance_Type type;

    public InsurancePK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsurancePK insurancePK = (InsurancePK) o;
        return id.equals(insurancePK.id) &&
                employee.equals(insurancePK.employee) &&
                type.equals(insurancePK.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employee, type);
    }
}
