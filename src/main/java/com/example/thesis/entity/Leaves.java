package com.example.thesis.entity;

import com.example.thesis.key.LeavesPK;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@IdClass(LeavesPK.class)
public class Leaves {
    @Id
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @Id
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Leave_Type.class)
    @JoinColumn(name="typeid", referencedColumnName="id")
    private Leave_Type type;

    private LocalDate from_date;

    private LocalDate to_date;

    @Lob
    private String description;
}
