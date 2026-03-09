package com.educonnect.exception.custom_exceptions;

/**
 * Thrown if attempting to create demographics for a student who already has them.
 */
public class DemographicsAlreadyExistsException extends RuntimeException {
    public DemographicsAlreadyExistsException(String message) {
        super(message);
    }
}