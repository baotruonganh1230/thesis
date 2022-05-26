package com.example.thesis.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Manage {
    @Id
    private Long did;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL, targetEntity = Department.class)
    @JoinColumn(name="did", referencedColumnName="id")
    private Department department;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;
}
