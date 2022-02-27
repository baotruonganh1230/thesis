package com.example.thesis.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate start_date;

    private LocalDate end_date;

    @OneToOne(
            mappedBy = "project",
            cascade = CascadeType.ALL
    )
    private Managed_By managed_by;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL
    )
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL
    )
    private List<Works_On> works_ons = new ArrayList<>();

    public Project(){

    }
}
