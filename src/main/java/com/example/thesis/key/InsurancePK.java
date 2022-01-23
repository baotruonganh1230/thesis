package com.example.thesis.key;

import java.io.Serializable;
import java.util.Objects;

public class InsurancePK implements Serializable {
    private Long id;
    private Long eid;
    private Long typeid;

    public InsurancePK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsurancePK insurancePK = (InsurancePK) o;
        return id.equals(insurancePK.id) &&
                eid.equals(insurancePK.eid) &&
                typeid.equals(insurancePK.typeid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eid, typeid);
    }
}
