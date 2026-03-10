package com.educonnect.service.contract;

import com.educonnect.dto.demographics.DemographicsRequestDTO;
import com.educonnect.model.demographics.StudentDemographics;

import java.util.UUID;

/**
 * Service interface for handling Student Demographics operations.
 */
public interface StudentDemographicsService {

    /**
     * Saves new demographics for an existing student.
     */
    StudentDemographics saveDemographics(UUID studentId, DemographicsRequestDTO requestDTO);

    /**
     * Updates existing demographics for a student.
     */
    StudentDemographics updateDemographics(UUID studentId, DemographicsRequestDTO requestDTO);

    /**
     * Retrieves demographics by student ID.
     */
    StudentDemographics getDemographics(UUID studentId);
}