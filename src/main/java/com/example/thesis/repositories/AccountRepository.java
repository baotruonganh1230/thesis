package com.example.thesis.repositories;

import com.example.thesis.entity.Account;
import com.example.thesis.key.AccountPK;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, AccountPK> {
    Optional<Account> findByEidAndRoleidAndPassword(Long eid, Long roleid, String password);
}
