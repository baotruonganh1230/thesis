package com.example.thesis.requests;

import com.example.thesis.responses.Bonus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobDetailInputParams {
    private LocalDate joinDate;
    private Long jobId;
    private String pit;
    private Long departmentId;
    private Integer salaryGroup;
    private BigDecimal salary;
    private Long shiftId;
    @JsonProperty("bonus")
    private List<Bonus> bonus;

    public void setJoinDate(String joinDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.joinDate = LocalDate.parse(joinDateString, formatter);
    }
}
