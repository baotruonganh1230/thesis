package com.example.thesis.keys;

import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
public class InsurancePK implements Serializable {
    private Long eid;
    private Long typeid;

    public InsurancePK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsurancePK insurancePK = (InsurancePK) o;
        return eid.equals(insurancePK.eid) &&
                typeid.equals(insurancePK.typeid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid, typeid);
    }
}
