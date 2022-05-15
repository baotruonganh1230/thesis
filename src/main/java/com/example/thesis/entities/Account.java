package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Role.class)
    @JoinColumn(name="roleid", referencedColumnName="id")
    private Role role;

    private String username;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL
    )
    private Set<AccountEventRel> accountEventRels;

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
