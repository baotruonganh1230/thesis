package com.example.thesis.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Leave_Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean is_paid;

    @OneToMany(
            mappedBy = "type",
            cascade = CascadeType.ALL
    )
    private List<Leaves> leaves = new ArrayList<>();
}
