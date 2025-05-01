package com.teamtrack.converter;

import com.teamtrack.dto.SpecialityDTO;
import com.teamtrack.dto.UserDTO;
import com.teamtrack.entity.Speciality;
import com.teamtrack.entity.User;

import java.util.Collections;
import java.util.stream.Collectors;

public class UserMapper {

    public static User dtoToModel(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .phone(userDTO.getPhone())
                .preName(userDTO.getPreName())
                .role(RoleMapper.dtoToModel(userDTO.getRole()))
                .state(userDTO.getState())
                .speciality(userDTO.getSpeciality().stream()
                        .map(SpecialityMapper::dtoToModel)
                        .collect(Collectors.toList()))
                .createdAt(userDTO.getCreatedAt())
                .build();
    }

    public static UserDTO modelToDto(User user) {
            return UserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .password(user.getPassword())
                    .preName(user.getPreName())
                    .role(RoleMapper.modelToDto((user.getRole())))
                    .state(user.getState())
                    .speciality(user.getSpeciality().stream()
                            .map(SpecialityMapper::modelToDto)
                            .collect(Collectors.toList()))
                    .createdAt(user.getCreatedAt())

                    .build();

    }
}
