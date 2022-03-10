package com.example.thesis.repositories;

import com.example.thesis.entities.Bonus_List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Bonus_ListRepository extends JpaRepository<Bonus_List, Long> {
}
