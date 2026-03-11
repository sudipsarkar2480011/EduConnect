package com.educonnect.exception.custom_exceptions;

public class InvalidUserException extends Exception{
    public InvalidUserException(String message){
        super(message);
    }
}
