package com.educonnect.service.contract;

import com.educonnect.dto.studentdetails.StudentDetailsDTO;

import java.util.UUID;

public interface StudentDetailsService {
    public StudentDetailsDTO updateStudentDetails(UUID studentUuid,StudentDetailsDTO studentDetailsDTO);
}
