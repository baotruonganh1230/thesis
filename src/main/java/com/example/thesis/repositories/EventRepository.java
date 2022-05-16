package com.example.thesis.repositories;

import com.example.thesis.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "select * from event where time >= ?1 and time <= ?2", nativeQuery = true)
    List<Event> getEventsByTimePeriod(LocalDateTime fromDateTime, LocalDateTime toDateTime);

    @Transactional
    @Modifying
    @Query(value = "update event set notes = ?2, title = ?3, time = ?4 where id = ?1", nativeQuery = true)
    int updateEventById(Long id, String notes, String title, LocalDateTime time);


}
