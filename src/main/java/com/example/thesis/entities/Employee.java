package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private String firstName;

    private String lastName;

    private String email;

    private String permanent_address;

    private String temporary_address;

    private String phone;

    @Column(precision = 16, scale = 2)
    private BigDecimal gross_salary;

    private LocalDate employed_date;

    private String sex;

    private LocalDate date_of_birth;

    private String pit;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    public Employee(String firstName, String lastName, String email, String permanent_address, String temporary_address, String phone, String sex, LocalDate date_of_birth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.permanent_address = permanent_address;
        this.temporary_address = temporary_address;
        this.phone = phone;
        this.sex = sex;
        this.date_of_birth = date_of_birth;
    }

    public Employee(String firstName, String lastName, String email, String permanent_address, String temporary_address, String phone, BigDecimal gross_salary, LocalDate employed_date, String sex, LocalDate date_of_birth, String pit, String avatar, Position position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.permanent_address = permanent_address;
        this.temporary_address = temporary_address;
        this.phone = phone;
        this.gross_salary = gross_salary;
        this.employed_date = employed_date;
        this.sex = sex;
        this.date_of_birth = date_of_birth;
        this.pit = pit;
        this.avatar = avatar;
        this.position = position;
    }

    @JsonIgnore
    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private List<Attendance> attendances = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private List<Payment> payments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private List<Leaves> leaves = new ArrayList<>();

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL, targetEntity = Position.class)
    @JoinColumn(referencedColumnName="id")
    private Position position;

    @JsonIgnore
    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private Manage manage;

    @JsonIgnore
    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private Managed_By managed_by;

    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private Set<Insurance> insurances = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "Performs",
            joinColumns = @JoinColumn(name = "eid"),
            inverseJoinColumns = {
                    @JoinColumn(name = "taskid", referencedColumnName = "id", nullable = false),
                    @JoinColumn(name = "pid", referencedColumnName = "pid", nullable = false)
            }
    )
    private Set<Task> tasks = new HashSet<>();

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private Works_In worksIn;

    @JsonIgnore
    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    private Works_On works_on;

    @Fetch(FetchMode.JOIN)
    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Bonus_List> bonus_lists =  new HashSet<>();

    public Employee() {

    }

    public void setDate_of_birth(String dateOfBirthString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.date_of_birth = LocalDate.parse(dateOfBirthString, formatter);
    }

    public void setEmployed_date(String employed_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.employed_date = LocalDate.parse(employed_dateString, formatter);
    }
}
