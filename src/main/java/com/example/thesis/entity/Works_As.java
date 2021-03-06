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
public class Works_As implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Position.class)
    @JoinColumn(name="posid", referencedColumnName="id")
    private Position position;
}
