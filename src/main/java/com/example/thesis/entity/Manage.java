package com.example.thesis.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"employee"})
public class Manage implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Department.class)
    @JoinColumn(name="did", referencedColumnName="id")
    private Department department;

    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;
}
