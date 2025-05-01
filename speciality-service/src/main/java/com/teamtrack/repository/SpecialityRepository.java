package com.teamtrack.repository;

import com.teamtrack.entity.Role;
import com.teamtrack.entity.Speciality;
import com.teamtrack.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    List<Speciality> findByDeletedFalse();
    Optional<Speciality> findByIdAndDeletedFalse(Long id);
}
