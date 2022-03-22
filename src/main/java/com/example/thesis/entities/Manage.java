package com.example.thesis.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"employee"})
public class Manage implements Serializable {
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
