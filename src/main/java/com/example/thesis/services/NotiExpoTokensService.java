package com.example.thesis.services;

import com.example.thesis.entities.NotiExpoTokens;
import com.example.thesis.repositories.NotiExpoTokensRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotiExpoTokensService {
    private NotiExpoTokensRepository notiExpoTokensRepository;

    public List<NotiExpoTokens> getAllNotiExpoTokens() {
        return notiExpoTokensRepository.findAll();
    }
}
