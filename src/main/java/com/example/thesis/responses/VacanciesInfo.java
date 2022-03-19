package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VacanciesInfo {
    private Long id;
    private Long hiringManagerId;
    private Long positionId;
    private Long departmentId;
    private LocalDate publishedDate;
    private LocalDate expiredDate;
    private Integer quantity;
    private Integer status;
    private String postContent;
}
