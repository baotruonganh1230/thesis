package com.example.thesis.services;

import com.example.thesis.entities.Account;
import com.example.thesis.repositories.AccountRepository;
import com.example.thesis.responses.AccountResponse;
import com.google.common.collect.Lists;
import com.mysql.cj.exceptions.DataReadException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {
    private static final String USER_NOT_FOUND_MSG = "user with email %s not found";

    private final AccountRepository accountRepository;

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
        return accountRepository.save(account);
    }

    public List<AccountResponse> getAccountResponses() {
        List<AccountResponse> accountResponses = new ArrayList<>();

        Lists.newArrayList(accountRepository.findAll()).forEach((Account account) -> {
            AccountResponse accountResponse = new AccountResponse(
                    account.getEid(),
                    account.getUsername(),
                    account.getRoleid(),
                    account.getPassword(),
                    account.getStatus().toString());

            accountResponses.add(accountResponse);
        });

        return accountResponses;
    }

    public AccountResponse getAccountByEidAndRoleid(Long eid, Long roleid) {
        Account account = accountRepository.findByEidAndRoleid(eid, roleid);
        AccountResponse accountResponse = new AccountResponse(
                account.getEid(),
                account.getUsername(),
                account.getRoleid(),
                account.getPassword(),
                account.getStatus().toString());
        return accountResponse;
    }

    public int updateAccountByEidAndRoleid(Long eid, Long roleid, Account account) {
        Account oldAccount = accountRepository.findByEidAndRoleid(eid, roleid);
        if (oldAccount == null) {
            throw new DataReadException("There is no account with that eid and roleid");
        }
        return accountRepository.setAccountByEidAndRoleid(
                eid,
                roleid,
                account.getEid(),
                account.getRoleid(),
                account.getPassword(),
                account.getStatus(),
                account.getUsername());

    }

    public void insertAccountByEidAndRoleid(Long eid, Long roleid, Account account) {
        Account oldAccount = accountRepository.findByEidAndRoleid(eid, roleid);
        if (oldAccount != null) {
            throw new EntityExistsException("Key already exists!");
        }
        accountRepository.insertAccountByEidAndRoleid(
                account.getEid(),
                account.getRoleid(),
                account.getPassword(),
                account.getStatus(),
                account.getUsername());
    }

    public Long count() {
        return accountRepository.count();
    }
}
