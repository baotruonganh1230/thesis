package com.example.thesis.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate created_time;

    @Fetch(FetchMode.JOIN)
    @OneToMany(
            mappedBy = "payroll",
            cascade = CascadeType.ALL
    )
    private Set<Payment> payments = new HashSet<>();
}
