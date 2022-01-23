package com.example.thesis.repositories;

import com.example.thesis.entity.Attendance;
import org.springframework.data.repository.CrudRepository;

public interface AttendanceRepository extends CrudRepository<Attendance, Long> {
}
