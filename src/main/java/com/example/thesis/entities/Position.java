package com.example.thesis.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(precision = 16, scale = 2)
    private BigDecimal min_salary;

    @Column(precision = 16, scale = 2)
    private BigDecimal max_salary;

    private Integer salaryGroup;

    private String description;

    private String note;

    @OneToOne(
            mappedBy = "position",
            cascade = CascadeType.ALL
    )
    private Has has;

    @OneToMany(
            mappedBy = "position",
            cascade = CascadeType.ALL
    )
    private List<Employee> employees = new ArrayList<>();

    public Position() {
    }

    public Position(String name, BigDecimal min_salary, BigDecimal max_salary) {
        this.name = name;
        this.min_salary = min_salary;
        this.max_salary = max_salary;
    }
}
