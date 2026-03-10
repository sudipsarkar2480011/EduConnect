package com.educonnect.exception.custom_exceptions;

public class ComplianceRecordNotFoundException extends RuntimeException {
    public ComplianceRecordNotFoundException(String message) {
        super(message);
    }
}
