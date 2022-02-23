package com.example.thesis.service;

import com.example.thesis.entity.Account;
import com.example.thesis.registration.token.ConfirmationToken;
import com.example.thesis.registration.token.ConfirmationTokenService;
import com.example.thesis.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {
    private static final String USER_NOT_FOUND_MSG = "user with email %s not found";

    private final AccountRepository accountRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }

    public String signUpAccount(Account account) {
        boolean usernameExists = accountRepository.findByUsername(account.getUsername()).isPresent();

        if (usernameExists) {
            throw new IllegalStateException("username already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(account.getPassword());

        account.setPassword(encodedPassword);

        save(account);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                account
        );

        // TODO: SEND EMAIL

        return confirmationTokenService.saveConfirmationToken(confirmationToken).getToken();
    }

    public int enableAccount(String username) {
        return accountRepository.enableAccount(username);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }
}
