package com.example.thesis.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bonus {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("bonusName")
    private String bonusName;
    @JsonProperty("bonusAmount")
    private BigDecimal bonusAmount;
}

