package com.brunols.virtual_menu.infra;

import com.brunols.virtual_menu.exceptions.NoItemsFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoItemsFoundException.class)
    public ResponseEntity<String> handleNoItemsFoundException(NoItemsFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
