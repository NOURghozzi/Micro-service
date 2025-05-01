package com.teamtrack.repository;

import com.teamtrack.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ProjectRepository extends JpaRepository <Project, Long>{

     Optional <Project> findByIdAndDeletedFalse(Long id) ;

     List<Project> findByDeletedFalse();


    List<Project> findByTeamIdAndDeletedFalse(Long id);

   // @Query("SELECT p FROM Project p WHERE p.name = :name AND p.deleted = false")
   // Optional<Project> findByNameAndDeletedFalse(@Param("name") String name);

    Optional<Project> findByNameAndDeletedFalse(String name);

    List<Project> findByTeamIdInAndDeletedFalse(List<Long> teamIds);
}
