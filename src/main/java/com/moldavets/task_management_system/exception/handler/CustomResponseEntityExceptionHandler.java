package com.moldavets.task_management_system.exception.handler;

import com.moldavets.task_management_system.exception.ResourceNotFoundException;
import com.moldavets.task_management_system.exception.UnauthorizedException;
import com.moldavets.task_management_system.exception.model.ExceptionDetailsModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetailsModel> handleAllExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDetailsModel> handleNullPointerException(NullPointerException ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetailsModel> handleResourceNotFoundException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionDetailsModel> handleUnauthorizedException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetailsModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        return new ResponseEntity<>(new ExceptionDetailsModel(
                LocalDateTime.now(),
                Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage(),
                request.getDescription(false)
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDetailsModel> handleMethodArgumentTypeMismatchException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ExceptionDetailsModel> handleMissingPathVariableException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(createExceptionDetailsModel(ex, request), HttpStatus.BAD_REQUEST);
    }

    private ExceptionDetailsModel createExceptionDetailsModel(Exception ex, WebRequest request) {
        return new ExceptionDetailsModel(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }
}
