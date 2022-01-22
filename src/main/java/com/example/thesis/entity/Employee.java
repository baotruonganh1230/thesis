package com.example.thesis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String first_name;

    private String last_name;

    private String email;

    private String permanent_address;

    private String temporary_address;

    private String phone;

    @Column(precision = 16, scale = 2)
    private BigDecimal gross_salary;

    @Column(precision = 16, scale = 2)
    private BigDecimal bonus;

    private LocalDate employed_date;

    private String sex;

    private LocalDate date_of_birth;

    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private Account account;

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private List<Leaves> leaves = new ArrayList<>();

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private List<Manage> manages;

    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private Managed_By managed_by;

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private List<Insurance> insurances = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "Performs",
            joinColumns = @JoinColumn(name = "eid"),
            inverseJoinColumns = {
                    @JoinColumn(name = "taskid", referencedColumnName = "id", nullable = false),
                    @JoinColumn(name = "pid", referencedColumnName = "pid", nullable = false)
            }
    )
    private Set<Task> tasks = new HashSet<>();

    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private Works_As works_as;

    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private Works_In works_in;

    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private Works_On works_on;

    public Employee() {

    }
}
