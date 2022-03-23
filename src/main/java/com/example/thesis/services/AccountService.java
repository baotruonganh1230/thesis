package com.example.thesis.services;

import com.example.thesis.entities.Account;
import com.example.thesis.repositories.AccountRepository;
import com.example.thesis.repositories.EmployeeRepository;
import com.example.thesis.repositories.RoleRepository;
import com.example.thesis.requests.AccountRequest;
import com.example.thesis.responses.AccountResponse;
import com.google.common.collect.Lists;
import com.mysql.cj.exceptions.DataReadException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {
    private static final String USER_NOT_FOUND_MSG = "user with email %s not found";

    private final AccountRepository accountRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final EmployeeRepository employeeRepository;

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }
        return account;
    }

    public String signUpAccount(Account account) {
        boolean usernameExists = (accountRepository.findByUsername(account.getUsername()) != null);

        if (usernameExists) {
            throw new IllegalStateException("username already taken");
        }

        save(account);


        // TODO: SEND EMAIL

        return "";
    }

    public int enableAccount(String username) {
        return accountRepository.enableAccount(username);
    }

    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public List<AccountResponse> getAccountResponses() {
        List<AccountResponse> accountResponses = new ArrayList<>();

        Lists.newArrayList(accountRepository.findAll()).forEach((Account account) -> {
            AccountResponse accountResponse = new AccountResponse(
                    account.getId(),
                    account.getEmployee() == null ? null : account.getEmployee().getId(),
                    account.getRole() == null ? null : account.getRole().getId(),
                    account.getUsername(),
                    account.getPassword(),
                    account.getStatus() == null ? null : account.getStatus().toString());

            accountResponses.add(accountResponse);
        });

        return accountResponses;
    }

    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.getByid(id);
        return new AccountResponse(
                account.getId(),
                account.getEmployee() == null ? null : account.getEmployee().getId(),
                account.getRole() == null ? null : account.getRole().getId(),
                account.getUsername(),
                account.getPassword(),
                account.getStatus() == null ? null : account.getStatus().toString());
    }

    public int updateAccountById(Long id, AccountRequest accountRequest) {
        Account oldAccount = accountRepository.getByid(id);
        if (oldAccount == null) {
            throw new DataReadException("There is no account with that id");
        }
        System.out.println();
        return accountRepository.setAccountById(
                id,
                accountRequest.getEid(),
                accountRequest.getRoleid(),
                accountRequest.getPassword(),
                accountRequest.getStatus().toString(),
                accountRequest.getUsername());
    }

    public void insertAccount(AccountRequest accountRequest) {
        accountRepository.save(
                new Account(
                        null,
                        accountRequest.getEid() == null ? null : employeeRepository.getById(accountRequest.getEid()),
                        roleRepository.getById(accountRequest.getRoleid()),
                        accountRequest.getUsername(),
                        accountRequest.getPassword(),
                        accountRequest.getStatus())
        );
    }

    public Long count() {
        return accountRepository.count();
    }

    public Account findByUsername(String username){
        return accountRepository.findByUsername(username);
    }
}
