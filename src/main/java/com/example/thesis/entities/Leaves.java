package com.example.thesis.entities;

import com.example.thesis.keys.LeavesPK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@IdClass(LeavesPK.class)
public class Leaves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    private Long eid;

    @Id
    private Long typeid;

    @JsonIgnore
    @MapsId
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @JsonIgnore
    @MapsId
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Leave_Type.class)
    @JoinColumn(name="typeid", referencedColumnName="id")
    private Leave_Type type;

    private LocalDate from_date;

    private LocalDate to_date;

    private LocalDate application_date;

    private Integer total;

    private Integer status;

    @Column(columnDefinition = "TEXT")
    private String reason;

    public Leaves(Long eid, Long typeid, Employee employee, Leave_Type type, LocalDate from_date, LocalDate to_date, LocalDate application_date, Integer total, Integer status, String reason) {
        this.eid = eid;
        this.typeid = typeid;
        this.employee = employee;
        this.type = type;
        this.from_date = from_date;
        this.to_date = to_date;
        this.application_date = application_date;
        this.total = total;
        this.status = status;
        this.reason = reason;
    }
}
