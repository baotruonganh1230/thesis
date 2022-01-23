package com.example.thesis.key;

import com.example.thesis.entity.Employee;
import com.example.thesis.entity.Role;

import java.io.Serializable;
import java.util.Objects;

public class AccountPK implements Serializable {
    private Long eid;
    private Long roleid;

    public AccountPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountPK accountPK = (AccountPK) o;
        return eid.equals(accountPK.eid) &&
                roleid.equals(accountPK.roleid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid, roleid);
    }
}
