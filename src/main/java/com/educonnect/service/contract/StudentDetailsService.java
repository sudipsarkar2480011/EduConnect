package com.educonnect.service.contract;

import com.educonnect.dto.studentdetails.StudentDetailsDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;

import java.util.UUID;

public interface StudentDetailsService {
    public StudentDetailsDTO updateStudentDetails(UUID studentUuid,StudentDetailsDTO studentDetailsDTO) throws UserNotFoundException;
}
