package com.example.thesis.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HaveCheckedInResponse {
    private boolean isCheckedIn;
    private String timeIn;
    private String timeOut;
}
