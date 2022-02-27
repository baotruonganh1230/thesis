package com.example.thesis.controllers;

import com.example.thesis.entities.Account;
import com.example.thesis.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("account/{eid}/{roleid}")
    public ResponseEntity<?> getAccount(@PathVariable Long eid, @PathVariable Long roleid) {
        return new ResponseEntity<>(acccountService.getAccountByEidAndRoleid(eid, roleid), HttpStatus.OK);
    }

    @PutMapping("account/{eid}/{roleid}")
    public ResponseEntity<?> updateAccount(@PathVariable Long eid,
                                           @PathVariable Long roleid,
                                           @RequestBody Account account) {
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        acccountService.updateAccountByEidAndRoleid(eid, roleid, account);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("account/{eid}/{roleid}")
    public ResponseEntity<?> insertAccount(@PathVariable Long eid,
                                           @PathVariable Long roleid,
                                           @RequestBody Account account) {
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        acccountService.insertAccountByEidAndRoleid(eid, roleid, account);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
