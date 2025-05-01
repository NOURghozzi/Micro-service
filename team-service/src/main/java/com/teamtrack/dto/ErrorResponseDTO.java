package com.teamtrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class ErrorResponseDTO {
    private int statusCode;
    private String message;
    private List<String> errors;



}
