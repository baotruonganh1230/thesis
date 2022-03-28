package com.example.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Checkin {
    @Id
    private Long attendanceId;

    private Integer status;

    private LocalDate date;

    private LocalTime time_in;
    private LocalTime time_out;

    private String deviceId;

    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    @MapsId
    @ManyToOne(cascade = CascadeType.REMOVE, targetEntity = Attendance.class)
    @JoinColumn(name="attendanceId", referencedColumnName="id")
    private Attendance attendance;
}
