package com.teamtrack.converter;

import com.teamtrack.dto.SpecialityDTO;
import com.teamtrack.entity.Speciality;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecialityMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public SpecialityMapper(ModelMapper modelMapper) {
        this.modelMapper  = modelMapper;
    }

    public static Speciality dtoToModel(SpecialityDTO specialityDTO) {
        return modelMapper.map(specialityDTO, Speciality.class);
    }

    public static SpecialityDTO modelToDto(Speciality speciality) {
        return modelMapper.map(speciality, SpecialityDTO.class);
    }

}
