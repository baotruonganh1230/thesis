package com.example.thesis.key;

import com.example.thesis.entity.Project;

import java.io.Serializable;
import java.util.Objects;

public class TaskPK implements Serializable {
    private Long id;
    private Project project;

    public TaskPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskPK taskPK = (TaskPK) o;
        return id.equals(taskPK.id) &&
                project.equals(taskPK.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, project);
    }
}
