package com.teamtrack.repository;

import com.teamtrack.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SprintRepository extends JpaRepository <Sprint, Long> {
    List<Sprint> findByDeletedFalse();
   Optional<Sprint> findByIdAndDeletedFalse(Long id);
    List<Sprint> findByProjectId(Long projectId);

    List<Sprint> findByProjectIdAndDeletedFalse(Long projectId);
}
