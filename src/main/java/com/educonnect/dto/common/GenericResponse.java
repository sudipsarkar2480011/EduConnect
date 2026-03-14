package com.educonnect.dto.common;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericResponse<T> {
    private T data;
    private String message;
    private int httpStatus;
}
