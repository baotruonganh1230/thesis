package com.example.thesis.keys;

import java.io.Serializable;
import java.util.Objects;

public class TaskPK implements Serializable {
    private Long id;
    private Long pid;

    public TaskPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskPK taskPK = (TaskPK) o;
        return id.equals(taskPK.id) &&
                pid.equals(taskPK.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pid);
    }
}
