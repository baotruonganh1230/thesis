package com.example.thesis.entity;

import com.example.thesis.key.TaskPK;
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

    @MapsId
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Project.class)
    @JoinColumn(name="pid", referencedColumnName="id")
    private Project project;

    @ManyToMany(mappedBy = "tasks")
    private Set<Employee> employees;
}
