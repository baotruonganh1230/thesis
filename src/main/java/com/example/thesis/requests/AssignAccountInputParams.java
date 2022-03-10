package com.example.thesis.requests;

import com.example.thesis.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignAccountInputParams {
    private String type;
    private Long employeeId;
    private Long roleId;
    private Account newAccount;
}
