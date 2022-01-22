package com.example.thesis.entity;

import com.example.thesis.key.InsurancePK;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@IdClass(InsurancePK.class)
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @Id
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Insurance_Type.class)
    @JoinColumn(name="typeid", referencedColumnName="id")
    private Insurance_Type type;

    private LocalDate start_date;

    private LocalDate expired_date;

    private String hospital;
}
