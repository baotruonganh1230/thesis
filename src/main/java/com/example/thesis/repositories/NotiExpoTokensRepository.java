package com.example.thesis.repositories;

import com.example.thesis.entities.NotiExpoTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotiExpoTokensRepository extends JpaRepository<NotiExpoTokens, Long> {
}
