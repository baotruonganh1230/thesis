package com.example.thesis.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Job_Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String job_description;

    @Column(columnDefinition = "TEXT")
    private String note;

    private LocalDate from_date;

    private LocalDate to_date;

    private Integer status;

    @OneToOne(
            mappedBy = "job_recruitment",
            cascade = CascadeType.ALL
    )
    private Has has;

    @ManyToMany
    @JoinTable(name = "Apply",
            joinColumns = @JoinColumn(name = "jrecruitid"),
            inverseJoinColumns = @JoinColumn(name = "cid"))
    private Set<Candidate> candidates = new HashSet<>();

    public Job_Recruitment() {
    }

    public Job_Recruitment(String job_description, LocalDate from_date, LocalDate to_date) {
        this.job_description = job_description;
        this.from_date = from_date;
        this.to_date = to_date;
    }

    public void setFrom_date(String from_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.from_date = LocalDate.parse(from_dateString, formatter);
    }

    public void setTo_date(String to_dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.to_date = LocalDate.parse(to_dateString, formatter);
    }
}
