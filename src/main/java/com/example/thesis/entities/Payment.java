package com.example.thesis.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"employee"})
public class Payment implements Serializable {
    @Id
    private Long eid;

    @MapsId
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @Column(precision = 16, scale = 2)
    private BigDecimal personal_tax;

    @Column(precision = 16, scale = 2)
    private BigDecimal derived_salary;

    private LocalDate payment_date;

    @Column(precision = 16, scale = 2)
    private BigDecimal other_income;

    @Column(precision = 16, scale = 2)
    private BigDecimal mandatory_insurance;

    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Payroll.class)
    @JoinColumn(name="payroll_id", referencedColumnName="id")
    private Payroll payroll;
}
