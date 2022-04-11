package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @MapsId
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @Column(precision = 16, scale = 2)
    private BigDecimal basicSalary;

    private LocalDate payment_date;

    private String actualDay;

    private String standardDay;

    private String paidLeave;

    private String unpaidLeave;

    @Column(precision = 16, scale = 2)
    private BigDecimal lunch;

    @Column(precision = 16, scale = 2)
    private BigDecimal parking;

    @Column(precision = 16, scale = 2)
    private BigDecimal allowanceNotSubjectedToTax;

    @Column(precision = 16, scale = 2)
    private BigDecimal personalRelief;

    @Column(precision = 16, scale = 2)
    private BigDecimal dependentRelief;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Payroll.class)
    @JoinColumn(name="payroll_id", referencedColumnName="id")
    private Payroll payroll;

}
