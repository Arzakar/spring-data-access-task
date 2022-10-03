package com.rntgroup.exception;

import com.rntgroup.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handle(NotFoundException exception) {
        ErrorDto error = new ErrorDto()
                .setStatus(HttpStatus.NOT_FOUND)
                .setMessage(exception.getMessage())
                .setDetails(exception.getCause());

        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handle(BadRequestException exception) {
        ErrorDto error = new ErrorDto()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setMessage(exception.getMessage())
                .setDetails(exception.getCause());

        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(NotImplementedException.class)
    public ResponseEntity<ErrorDto> handle(NotImplementedException exception) {
        ErrorDto error = new ErrorDto()
                .setStatus(HttpStatus.NOT_IMPLEMENTED)
                .setMessage(exception.getMessage())
                .setDetails(exception.getCause());

        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handle(RuntimeException exception) {
        ErrorDto error = new ErrorDto()
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .setMessage(exception.getMessage())
                .setDetails(exception.getCause());

        return new ResponseEntity<>(error, error.getStatus());
    }
}
