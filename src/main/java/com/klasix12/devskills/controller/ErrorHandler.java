package com.klasix12.devskills.controller;

import com.klasix12.devskills.dto.ErrorResponseDto;
import com.klasix12.devskills.exception.EmailAlreadyExistsException;
import com.klasix12.devskills.exception.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return getError("Email already exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        return getError("Username already exists", HttpStatus.BAD_REQUEST);
    }


    private ErrorResponseDto getError(String message, HttpStatus status) {
        return ErrorResponseDto.builder()
                .message(message)
                .status(status.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
