package com.example.thesis.entities;

import com.example.thesis.keys.InsurancePK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@IdClass(InsurancePK.class)
@AllArgsConstructor
@NoArgsConstructor
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeid;

    @JsonIgnore
    @MapsId
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @JsonIgnore
    @MapsId
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Insurance_Type.class)
    @JoinColumn(name="typeid", referencedColumnName="id")
    private Insurance_Type type;

    private LocalDate from_date;

    private LocalDate to_date;

    private LocalDate issue_date;

    private String number;

    private Long cityId;

    private Long kcbId;

}
