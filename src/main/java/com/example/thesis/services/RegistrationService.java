package com.example.thesis.services;

import com.example.thesis.entities.Account;
import com.example.thesis.entities.AccountStatus;
import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Role;
import com.example.thesis.registration.EmailValidator;
import com.example.thesis.requests.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AccountService accountService;

    private final EmployeeService employeeService;

    private final RoleService roleService;

    private final EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid!");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        formatter = formatter.withLocale(Locale.US);

        Employee savedEmployee = employeeService.save(
                new Employee(request.getFirst_name(),
                        request.getLast_name(),
                        request.getEmail(),
                        request.getPermanent_address(),
                        request.getTemporary_address(),
                        request.getPhone(),
                        request.getSex(),
                        LocalDate.parse(request.getDate_of_birth(), formatter)));

        System.out.println(savedEmployee.getFirst_name());

        Role savedRole = roleService.save(new Role("USER", "User role"));

        return accountService.signUpAccount(new Account(
                savedEmployee.getId(),
                savedRole.getId(),
                savedEmployee,
                savedRole,
                request.getUsername(),
                request.getPassword(),
                AccountStatus.ENABLE));
    }

    @Transactional
    public String confirmToken(String token) {
        return "confirmed";
    }
}
