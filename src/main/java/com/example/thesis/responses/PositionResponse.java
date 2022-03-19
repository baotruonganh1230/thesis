package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionResponse {
    private Long id;
    private String title;
    private String description;
    private String note;
    private BigDecimal min_salary;
    private BigDecimal max_salary;
    private Integer salary_group;
}
