package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Role.class)
    @JoinColumn(name="roleid", referencedColumnName="id")
    private Role role;

    private String username;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    public Account(Employee employee, Role role, String username, String password, AccountStatus status) {
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
