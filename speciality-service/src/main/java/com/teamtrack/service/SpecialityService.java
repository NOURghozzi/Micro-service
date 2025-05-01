package com.teamtrack.service;

import com.teamtrack.converter.RoleMapper;
import com.teamtrack.converter.SpecialityMapper;
import com.teamtrack.dto.RoleDTO;
import com.teamtrack.dto.SpecialityDTO;
import com.teamtrack.entity.Role;
import com.teamtrack.entity.Speciality;
import com.teamtrack.entity.Sprint;
import com.teamtrack.exception.HttpCustomException;
import com.teamtrack.repository.RoleRepository;
import com.teamtrack.repository.SpecialityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SpecialityService {
    @Autowired
    private SpecialityRepository specialityRepository;

    public List<SpecialityDTO> getAllSpeciality() {
        log.info("Fetching all speciality");
        List<Speciality> speciality = specialityRepository.findByDeletedFalse();
        return speciality.stream().map(SpecialityMapper::modelToDto).collect(Collectors.toList());
    }

    public SpecialityDTO getSpeciality(Long id) {
        log.info("Fetching speciality");
        return SpecialityMapper.modelToDto(specialityRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Speciality does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value())));
    }

    public void deletespeciality (Long id) {
        log.info("Deleting speciality with ID: {}", id);
        Speciality speciality = specialityRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("speciality does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        speciality.setDeleted(true);
        specialityRepository.save(speciality);
    }
}
