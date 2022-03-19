package com.example.thesis.controllers;

import com.example.thesis.entities.Account;
import com.example.thesis.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class AccountController {
    private final AccountService acccountService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("accounts")
    public ResponseEntity<?> getAccounts() {
        return new ResponseEntity<>(acccountService.getAccountResponses(), HttpStatus.OK);
    }

    @GetMapping("account/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        return new ResponseEntity<>(acccountService.getAccountById(id), HttpStatus.OK);
    }

    @PutMapping("account/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id,
                                           @RequestBody Account account) {
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        acccountService.updateAccountById(id, account);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("account")
    public ResponseEntity<?> insertAccount(@RequestBody Account account) {
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        acccountService.insertAccount(account);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
