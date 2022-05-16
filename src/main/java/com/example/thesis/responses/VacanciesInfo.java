package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VacanciesInfo {
    private Long id;
    private Long hiringManagerId;
    private Long positionId;
    private Long departmentId;
    private String publishedDate;
    private String expiredDate;
    private Integer quantity;
    private Integer status;
    private String postContent;

    public VacanciesInfo(Long id, Long hiringManagerId, Long positionId, Long departmentId, LocalDate publishedDate, LocalDate expiredDate, Integer quantity, Integer status, String postContent) {
        this.id = id;
        this.hiringManagerId = hiringManagerId;
        this.positionId = positionId;
        this.departmentId = departmentId;
        this.publishedDate = publishedDate.toString() + "T00:00:00.000Z";
        this.expiredDate = expiredDate.toString() + "T00:00:00.000Z";
        this.quantity = quantity;
        this.status = status;
        this.postContent = postContent;
    }

    public void setPublishedDate(String publishedDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.publishedDate = LocalDate.parse(publishedDateString, formatter).toString();
    }

    public void setExpiredDate(String toDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //convert String to LocalDate
        this.expiredDate = LocalDate.parse(toDateString, formatter).toString();
    }
}
