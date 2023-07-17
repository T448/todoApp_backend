package com.example.spring_project.domain.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class SessionException extends RuntimeException {

    public SessionException(String message) {
        super(message);
    }
}