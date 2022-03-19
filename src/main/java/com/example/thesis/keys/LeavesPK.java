package com.example.thesis.keys;

import java.io.Serializable;
import java.util.Objects;

public class LeavesPK implements Serializable {
    private Long id;
    private Long eid;
    private Long typeid;

    public LeavesPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeavesPK leavesPK = (LeavesPK) o;
        return id.equals(leavesPK.id) && eid.equals(leavesPK.eid) &&
                typeid.equals(leavesPK.typeid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eid, typeid);
    }
}
