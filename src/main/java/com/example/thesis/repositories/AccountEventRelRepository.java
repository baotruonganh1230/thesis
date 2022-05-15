package com.example.thesis.repositories;

import com.example.thesis.entities.Account;
import com.example.thesis.entities.AccountEventRel;
import com.example.thesis.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountEventRelRepository extends JpaRepository<AccountEventRel, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into account_event_rel (accountid, eventid) values (?1, ?2)", nativeQuery = true)
    int insertAccountEventRel(Long accountid, Long eventid);

    @Query(value = "select accountid from account_event_rel where eventid = ?1", nativeQuery = true)
    List<Long> getAccountIdsByEventId(Long eventid);

    boolean existsByEventAndAccount(Event event, Account account);
}
