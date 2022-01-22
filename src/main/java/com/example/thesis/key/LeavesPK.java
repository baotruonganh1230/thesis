package com.example.thesis.key;

import com.example.thesis.entity.Employee;
import com.example.thesis.entity.Leave_Type;

import java.io.Serializable;
import java.util.Objects;

public class LeavesPK implements Serializable {
    private Employee employee;
    private Leave_Type type;

    public LeavesPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeavesPK leavesPK = (LeavesPK) o;
        return employee.equals(leavesPK.employee) &&
                type.equals(leavesPK.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, type);
    }
}
