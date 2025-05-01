package com.teamtrack.config;

import com.teamtrack.dto.ErrorResponseDTO;
import com.teamtrack.exception.HttpCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpCustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(HttpCustomException ex) {
        log.error("Resolved custom exception with error: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponseDTO.builder().statusCode(ex.getStatusCode()).message(ex.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        log.error("MethodArgumentNotValidException occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    /*
    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<?> handleInvalidUserIdException(InvalidUserIdException ex, HttpServletRequest request) {
        log.error("InvalidUserIdException occurred: {}",response(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
        return response(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException exception) {
        log.error("ResourceNotFoundException occurred: {}", exception.getMessage());
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<Object> resourceAlreadyExistException(ResourceAlreadyExistException exception) {
        log.error("ResourceAlreadyExistException occurred: {}", exception.getMessage());
        return response(HttpStatus.CONFLICT, exception.getMessage(), null);
    }

    private ResponseEntity<Object> response(HttpStatus status, String message, List<String> errors) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(status.value(), message, errors);
        return new ResponseEntity<>(errorResponse, status);
    }*/
}
