package com.example.thesis.entities;

import com.example.thesis.keys.TaskPK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@IdClass(TaskPK.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    private Long pid;

    private String status;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @JsonIgnore
    @MapsId
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Project.class)
    @JoinColumn(name="pid", referencedColumnName="id")
    private Project project;

    @JsonIgnore
    @ManyToMany(mappedBy = "tasks")
    private Set<Employee> employees;
}
