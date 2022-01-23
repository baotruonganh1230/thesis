package com.example.thesis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    private String name;

    @OneToOne(
            mappedBy = "department",
            cascade = CascadeType.ALL
    )
    private Manage manage;

    @OneToMany(
            mappedBy = "department",
            cascade = CascadeType.ALL
    )
    private List<Works_In> works_ins;

    public Department() {
    }

    public Department(String location, String name) {
        this.location = location;
        this.name = name;
    }
}
