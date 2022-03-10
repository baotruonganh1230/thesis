package com.example.thesis.entities;

import com.example.thesis.keys.AccountPK;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
@IdClass(AccountPK.class)
@NoArgsConstructor
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

    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    public Account(Long eid, Long roleid, Employee employee, Role role, String username, String password, AccountStatus status) {
        this.eid = eid;
        this.roleid = roleid;
        this.employee = employee;
        this.role = role;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return status == AccountStatus.ENABLE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == AccountStatus.ENABLE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status == AccountStatus.ENABLE;
    }

    @Override
    public boolean isEnabled() {
        return status == AccountStatus.ENABLE;
    }
}
