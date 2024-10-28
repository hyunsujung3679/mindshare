package com.hsj.aft.common.exception;

import com.hsj.aft.common.dto.CommonResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleException(Exception e, Locale locale) {
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return ResponseEntity.ok(CommonResponse.error(
                    String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()),
                    messageSource.getMessage("message.method.not.allowed", null, locale)
            ));
        } else if (e instanceof HttpMessageNotReadableException) {
            return ResponseEntity.ok(CommonResponse.error(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    messageSource.getMessage("message.bad.request", null, locale)
            ));
        } else {
            return ResponseEntity.ok(CommonResponse.error(
                    String.valueOf(HttpStatus.NOT_FOUND.value()),
                    messageSource.getMessage("message.api.not.found", null, locale)
            ));
        }
    }
}
