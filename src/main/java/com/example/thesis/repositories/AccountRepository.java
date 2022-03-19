package com.example.thesis.repositories;

import com.example.thesis.entities.Account;
import com.example.thesis.entities.AccountStatus;
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
    @Query(value = "update Account a set a.eid = ?2, a.roleid = ?3, a.password = ?4, a.status = ?5, a.username = ?6 where a.id = ?1", nativeQuery = true)
    int setAccountById(Long id,
                                   Long newEid,
                                   Long newRoleId,
                                   String newPassword,
                                   AccountStatus newStatus,
                                   String newUsername);

    Account getByid(Long id);
}
