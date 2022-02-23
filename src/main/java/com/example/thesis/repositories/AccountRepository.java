package com.example.thesis.repositories;

import com.example.thesis.entity.Account;
import com.example.thesis.key.AccountPK;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AccountRepository extends CrudRepository<Account, AccountPK> {
    Optional<Account> findByEidAndRoleidAndPassword(Long eid, Long roleid, String password);

    Optional<Account> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE Account a " +
            "SET a.enabled = TRUE WHERE a.username = ?1")
    int enableAccount(String username);
}
