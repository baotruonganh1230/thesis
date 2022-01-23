package com.example.thesis.key;

import java.io.Serializable;
import java.util.Objects;

public class LeavesPK implements Serializable {
    private Long eid;
    private Long typeid;

    public LeavesPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeavesPK leavesPK = (LeavesPK) o;
        return eid.equals(leavesPK.eid) &&
                typeid.equals(leavesPK.typeid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid, typeid);
    }
}
