package com.agency.finalproject.exception;

import com.agency.finalproject.entity.login.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class HandleValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleConflict() {
        log.error("some of the method arguments are not valid.");
        return new ResponseEntity<>(new MessageResponse("One of entity fields is not valid."), HttpStatus.BAD_REQUEST);
    }

}
