package com.example.thesis.entity;

import com.example.thesis.key.AccountPK;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@IdClass(AccountPK.class)
public class Account {
    @Id
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @Id
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Role.class)
    @JoinColumn(name="roleid", referencedColumnName="id")
    private Role role;

    private String username;

    private String password;
}
