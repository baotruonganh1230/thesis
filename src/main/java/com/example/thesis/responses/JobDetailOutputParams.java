package com.example.thesis.responses;

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
public class JobDetailOutputParams {
    private String joinDate;
    private Long jobId;
    private String pit;
    private Long departmentId;
    private Integer salaryGroup;
    private BigDecimal salary;
    @JsonProperty("bonus")
    private List<Bonus> bonus;

    public JobDetailOutputParams(LocalDate joinDate, Long jobId, String pit, Long departmentId, Integer salaryGroup, BigDecimal salary, List<Bonus> bonus) {
        this.joinDate = joinDate.toString() + "'T'00:00:00.000Z";
        this.jobId = jobId;
        this.pit = pit;
        this.departmentId = departmentId;
        this.salaryGroup = salaryGroup;
        this.salary = salary;
        this.bonus = bonus;
    }

    public void setJoinDate(String joinDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.joinDate = LocalDate.parse(joinDateString, formatter).toString();
    }
}
