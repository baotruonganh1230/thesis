package com.example.thesis.repositories;

import com.example.thesis.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE Account a " +
            "SET a.status = 'ENABLE' WHERE a.username = ?1")
    int enableAccount(String username);

    @Transactional
    @Modifying
    @Query(value = "update account set eid = ?2, roleid = ?3, password = ?4, status = ?5, username = ?6 where id = ?1", nativeQuery = true)
    int setAccountById(Long id,
                                   Long newEid,
                                   Long newRoleId,
                                   String newPassword,
                                   String newStatus,
                                   String newUsername);

    Account getByid(Long id);
}
