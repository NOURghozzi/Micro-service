package com.teamtrack.repository;

import com.teamtrack.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository <Team, Long>{
    Optional<Team> findByIdAndDeletedFalse(Long id) ;


    List<Team> findByDeletedFalse();


    Optional<Team> findByNameAndDeletedFalse(String name);

    List<Team> findAllByUserIdsContainsAndDeletedFalse(Long userId);

}
