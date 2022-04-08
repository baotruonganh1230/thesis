package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendStatusResponse {
    private Long onTime;
    private Long late;
    private Long onLeave;
}
