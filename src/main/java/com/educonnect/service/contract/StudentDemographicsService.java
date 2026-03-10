package com.educonnect.service.contract;

import com.educonnect.dto.demographics.DemographicsRequestDTO;
import com.educonnect.model.demographics.StudentDemographics;

import java.util.UUID;

/**
 * Service interface for handling Student Demographics operations.
 * Defines the contract for managing lifecycle events of student profile data.
 *
 * @author Sankha Subhra Chakraborty
 * @version 1.0
 * @since 1.0
 */
public interface StudentDemographicsService {

    /**
     * Persists new demographic details for a specific student in the database.
     * * @param studentId The unique identifier of the student to link the demographics to
     * @param requestDTO The data transfer object containing all demographic attributes
     * @return {@link StudentDemographics} The persisted demographic entity
     * @since 1.0
     */
    StudentDemographics saveDemographics(UUID studentId, DemographicsRequestDTO requestDTO);

    /**
     * Updates the existing demographic records for a student profile.
     * * @param studentId The unique identifier of the student whose record is being updated
     * @param requestDTO The data transfer object containing updated demographic information
     * @return {@link StudentDemographics} The updated demographic entity
     * @since 1.0
     */
    StudentDemographics updateDemographics(UUID studentId, DemographicsRequestDTO requestDTO);

    /**
     * Retrieves the demographic information associated with a student ID.
     * * @param studentId The unique identifier used to find the student's demographic record
     * @return {@link StudentDemographics} The demographic entity found in the database
     * @since 1.0
     */
    StudentDemographics getDemographics(UUID studentId);
}