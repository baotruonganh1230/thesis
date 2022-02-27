package com.example.thesis.services;

import com.example.thesis.entities.Account;
import com.example.thesis.entities.Employee;
import com.example.thesis.entities.Role;
import com.example.thesis.registration.EmailValidator;
import com.example.thesis.registration.token.ConfirmationToken;
import com.example.thesis.registration.token.ConfirmationTokenService;
import com.example.thesis.requests.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AccountService accountService;

    private final EmployeeService employeeService;

    private final RoleService roleService;

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid!");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
                request.getPassword()));
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        accountService.enableAccount(
                confirmationToken.getAccount().getUsername());
        return "confirmed";
    }
}
