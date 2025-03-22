package com.moldavets.task_management_system.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RegistrationException extends RuntimeException {
    public RegistrationException(String message) {
        super(message);
    }
}
