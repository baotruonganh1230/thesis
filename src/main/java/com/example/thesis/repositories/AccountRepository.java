package com.example.thesis.repositories;

import com.example.thesis.entities.Account;
import com.example.thesis.entities.AccountStatus;
import com.example.thesis.keys.AccountPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, AccountPK> {

    Account findByUsername(String username);

    Account findByEidAndRoleid(Long eid, Long roleid);

    @Transactional
    @Modifying
    @Query("UPDATE Account a " +
            "SET a.status = 'ENABLE' WHERE a.username = ?1")
    int enableAccount(String username);

    @Transactional
    @Modifying
    @Query("update Account a set a.eid = ?3, a.roleid = ?4, a.password = ?5, a.status = ?6, a.username = ?7 where a.eid = ?1 and a.roleid = ?2")
    int setAccountByEidAndRoleid(Long eid,
                                   Long roleid,
                                   Long newEid,
                                   Long roleId,
                                   String newPassword,
                                   AccountStatus newStatus,
                                   String newUsername);

    @Transactional
    @Modifying
    @Query(value = "insert into Account (eid, roleid, password, status, username) values (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    void insertAccountByEidAndRoleid(Long newEid,
                                 Long roleId,
                                 String newPassword,
                                 AccountStatus newStatus,
                                 String newUsername);
}
