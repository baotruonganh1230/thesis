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
public class Managed_By implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Project.class)
    @JoinColumn(name="pid", referencedColumnName="id")
    private Project project;
}
