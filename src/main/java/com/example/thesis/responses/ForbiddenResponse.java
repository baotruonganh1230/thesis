package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ForbiddenResponse {
    private LocalDateTime timestamp;
    private Long status;
    private String error;
    private String message;
    private String path;
}
