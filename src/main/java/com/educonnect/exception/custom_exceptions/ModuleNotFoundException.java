package com.educonnect.exception.custom_exceptions;

public class ModuleNotFoundException extends RuntimeException {
    public ModuleNotFoundException(String message) {
        super(message);
    }
}
