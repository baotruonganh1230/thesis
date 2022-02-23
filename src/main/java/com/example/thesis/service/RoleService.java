package com.example.thesis.service;

import com.example.thesis.entity.Role;
import com.example.thesis.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    @Transactional
    public Role save(Role role) {
        return repository.save(role);
    }
}
