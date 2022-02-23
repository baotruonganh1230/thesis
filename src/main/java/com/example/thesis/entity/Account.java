package com.example.thesis.entity;

import com.example.thesis.key.AccountPK;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
@IdClass(AccountPK.class)
public class Account implements UserDetails {
    @Id
    private Long eid;

    @Id
    private Long roleid;

    @MapsId
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @MapsId
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Role.class)
    @JoinColumn(name="roleid", referencedColumnName="id")
    private Role role;

    private String username;

    private String password;

    private Boolean locked = false;

    private Boolean enabled = false;

    public Account(Long eid, Long roleid, Employee employee, Role role, String username, String password) {
        this.eid = eid;
        this.roleid = roleid;
        this.employee = employee;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public Account() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
