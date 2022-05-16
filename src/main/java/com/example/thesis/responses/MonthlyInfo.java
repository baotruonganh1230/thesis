package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyInfo {
    private String actualDay;

    private String standardDay;

    private String paidLeave;

    private String unpaidLeave;
}
