package com.example.thesis.controllers;

import com.example.thesis.services.NotiExpoTokensService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class NotiExpoTokensController {
    private final NotiExpoTokensService notiExpoTokensService;

    @GetMapping("notiexpotokens")
    public ResponseEntity<?> getNotiExpoTokens() {
        return new ResponseEntity<>(notiExpoTokensService.getAllNotiExpoTokens(), HttpStatus.OK);
    }
}
