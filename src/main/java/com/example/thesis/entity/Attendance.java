package com.example.thesis.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"employee"})
public class Attendance implements Serializable {
    @Id
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    private LocalDate day;

    private LocalTime time_in;
    private LocalTime time_out;
}
