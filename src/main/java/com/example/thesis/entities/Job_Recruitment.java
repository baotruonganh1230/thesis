package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Job_Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String postContent;

    @Column(columnDefinition = "TEXT")
    private String note;

    private LocalDate published_date;

    private LocalDate expired_date;

    private Integer status;

    private Integer quantity;

    @Fetch(FetchMode.JOIN)
    @OneToOne(cascade = CascadeType.REMOVE, targetEntity = Employee.class)
    @JoinColumn(name="hiring_manager_id", referencedColumnName="id")
    private Employee employee;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(cascade=CascadeType.ALL, targetEntity = Department.class)
    @JoinColumn(name = "department_id", referencedColumnName="id")
    private Department department;

    @JsonIgnore
    @OneToOne(
            mappedBy = "job_recruitment",
            cascade = CascadeType.ALL
    )
    private Has has;

    @JsonIgnore
    @OneToMany(
            mappedBy = "job_recruitment",
            cascade = CascadeType.ALL
    )
    private List<Candidate> candidates = new ArrayList<>();

    public Job_Recruitment() {
    }

    public void setFrom_date(String from_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.published_date = LocalDate.parse(from_dateString, formatter);
    }

    public void setTo_date(String to_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.expired_date = LocalDate.parse(to_dateString, formatter);
    }
}
