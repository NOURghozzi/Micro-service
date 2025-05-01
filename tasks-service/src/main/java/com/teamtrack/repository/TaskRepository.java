package com.teamtrack.repository;

import com.teamtrack.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository <Task, Long> {
    List<Task> findByDeletedFalse();
    Optional<Task> findByIdAndDeletedFalse(Long id);

    List<Task> findByUserIdAndDeletedFalse(Long id);

    List<Task> findByProjectId(Long projectId);

    List<Task> findBySprintId(Long sprintId);
    List<Task> findBySprintIdAndDeletedFalse(Long id);

    Optional<Task> findByNameAndDeletedFalse(String name);

   // List<Task> findAllByUserAndDeletedFalse(Long id);
}
