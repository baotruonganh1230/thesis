package com.example.thesis.entities;

import com.example.thesis.keys.InsurancePK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@IdClass(InsurancePK.class)
public class Insurance {
    @Id
    private Long eid;

    @Id
    private Long typeid;

    @MapsId
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
    @JoinColumn(name="eid", referencedColumnName="id")
    private Employee employee;

    @MapsId
    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Insurance_Type.class)
    @JoinColumn(name="typeid", referencedColumnName="id")
    private Insurance_Type type;

    private LocalDate from_date;

    private LocalDate to_date;

    private LocalDate issue_date;

    private String number;

    private Long cityId;

    private Long kcbId;
}
