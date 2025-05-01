package com.teamtrack.controller;

import com.teamtrack.dto.SpecialityDTO;
import com.teamtrack.service.SpecialityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/speciality")
@AllArgsConstructor
public class SpecialityController {

    private SpecialityService specialityService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<SpecialityDTO>> getAllspeciality() {
        log.info("Fetching all Speciality");
        return ResponseEntity.ok(this.specialityService.getAllSpeciality());
    }

    @GetMapping("/find-speciality/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SpecialityDTO> getspeciality(@PathVariable Long id) {
        log.info("Fetching Speciality");
        return ResponseEntity.ok(specialityService.getSpeciality(id));
    }

}
