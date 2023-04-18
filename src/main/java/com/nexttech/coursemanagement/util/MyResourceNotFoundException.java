package com.nexttech.coursemanagement.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Asset not found.")
public class MyResourceNotFoundException extends RuntimeException{
    public MyResourceNotFoundException(String message) {
        super(message);
    }
}
