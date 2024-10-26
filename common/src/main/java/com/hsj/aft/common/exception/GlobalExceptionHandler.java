package com.hsj.aft.common.exception;

import com.hsj.aft.common.dto.CommonResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<CommonResponse> handleDuplicateIdException(DuplicateIdException e) {
        return ResponseEntity.ok(CommonResponse.error(String.valueOf(HttpStatus.CONFLICT.value()), e.getMessage()));
    }

    @ExceptionHandler(NoAuthorizationException.class)
    public ResponseEntity<CommonResponse> handleNoAuthorizationException(NoAuthorizationException e) {
        return ResponseEntity.ok(CommonResponse.error(String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.ok(CommonResponse.error(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getMessage()));
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<CommonResponse> handlePasswordNotMatchException(PasswordNotMatchException e) {
        return ResponseEntity.ok(CommonResponse.error(String.valueOf(HttpStatus.BAD_REQUEST.value()), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return ResponseEntity.ok(CommonResponse.error(String.valueOf(HttpStatus.BAD_REQUEST.value()), errors));
    }
}
