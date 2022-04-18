package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Leaves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Leave_Type.class)
    @JoinColumn(name="typeid", referencedColumnName="id")
    private Leave_Type type;

    private LocalDate fromDate;

    private LocalDate toDate;

    private LocalDate applicationDate;

    private Integer total;

    private Integer status;

    @Column(columnDefinition = "TEXT")
    private String reason;

    public Leaves(Employee employee, Leave_Type type, LocalDate fromDate, LocalDate toDate, LocalDate applicationDate, Integer total, Integer status, String reason) {
        this.employee = employee;
        this.type = type;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.applicationDate = applicationDate;
        this.total = total;
        this.status = status;
        this.reason = reason;
    }
}
