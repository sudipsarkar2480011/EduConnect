package com.educonnect.service.implementation;

import com.educonnect.dto.demographics.DemographicsRequestDTO;
import com.educonnect.exception.custom_exceptions.DemographicsAlreadyExistsException;
import com.educonnect.exception.custom_exceptions.DemographicsNotFoundException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.demographics.StudentDemographics;
import com.educonnect.model.user.Student;
import com.educonnect.repo.StudentRepo;
import com.educonnect.repo.demographics.StudentDemographicsRepo;
// Import your existing Student Repo. Adjust package if needed.
import org.springframework.data.jpa.repository.JpaRepository;
import com.educonnect.service.contract.StudentDemographicsService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of {@link StudentDemographicsService} to manage student profile data.
 * Handles the capture, update, and retrieval of legal and personal demographics.
 * * @author Sankha Subhra Chakraborty
 * @version 1.0
 * @since 1.0
 */

@Service
@RequiredArgsConstructor
public class StudentDemographicsServiceImpl implements StudentDemographicsService {

    private final StudentDemographicsRepo demographicsRepo;

    // Injecting your existing Student Repository to fetch the core student
    private final StudentRepo studentRepo;

    /**
     * Captures and persists new student demographics in the database.
     * Links the demographics record to an existing Student via their UUID.
     * * @param studentId The unique identifier of the student
     * @param requestDTO Data transfer object containing demographic details
     * @return {@link StudentDemographics} The persisted demographic entity
     * @throws UserNotFoundException If the student does not exist
     * @throws DemographicsAlreadyExistsException If a record already exists for the given student
     * @since 1.0
     */

    @Override
    @Transactional
    public StudentDemographics saveDemographics(UUID studentId, DemographicsRequestDTO requestDTO) {

        // 1. Validate that the core Student exists first
        Student student = null;
        try {
            student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new UserNotFoundException("Student not found with ID: " + studentId));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 2. Ensure we don't duplicate demographics for the same student
        if (demographicsRepo.existsById(studentId)) {
            throw new DemographicsAlreadyExistsException("Demographics already exist for student ID: " + studentId);
        }

        // 3. Map the DTO data to our Entity
        StudentDemographics demographics = mapToEntity(requestDTO);

        // 4. Set the relationship. Because we used @MapsId, saving this
        // will automatically use the Student's UUID as its Primary Key.
        demographics.setStudent(student);

        // 5. Save and return
        return demographicsRepo.save(demographics);
    }

    @Override
    @Transactional

    /**
     * Updates an existing student demographic record with new information.
     * * @param studentId The unique identifier of the student
     * @param requestDTO Data transfer object containing updated details
     * @return {@link StudentDemographics} The updated demographic entity
     * @throws DemographicsNotFoundException If no demographic record exists to update
     * @since 1.0
     */

    public StudentDemographics updateDemographics(UUID studentId, DemographicsRequestDTO requestDTO) {

        // 1. Fetch the existing demographics record
        StudentDemographics existingDemographics = demographicsRepo.findById(studentId)
                .orElseThrow(() -> new DemographicsNotFoundException("Demographics not found for student ID: " + studentId));

        // 2. Update the fields using the incoming DTO
        updateEntityFields(existingDemographics, requestDTO);

        // 3. Save the updated record
        return demographicsRepo.save(existingDemographics);
    }

    /**
     * Retrieves the demographic record for a specific student from the database.
     * * @param studentId The unique identifier of the student
     * @return {@link StudentDemographics} The found demographic entity
     * @throws DemographicsNotFoundException If no record is found for the student ID
     * @since 1.0
     */

    @Override
    public StudentDemographics getDemographics(UUID studentId) {
        return demographicsRepo.findById(studentId)
                .orElseThrow(() -> new DemographicsNotFoundException("Demographics not found for student ID: " + studentId));
    }

    // ==========================================
    // HELPER METHODS FOR MAPPING DTO -> ENTITY
    // ==========================================

    /**
     * Internal helper to map the Request DTO to the StudentDemographics Entity.
     * * @param dto The source data transfer object
     * @return {@link StudentDemographics} A newly built entity object
     * @since 1.0
     */
    private StudentDemographics mapToEntity(DemographicsRequestDTO dto) {
        return StudentDemographics.builder()
                .legalFullName(dto.legalFullName())
                .nationality(dto.nationality())
                .religion(dto.religion())
                .gender(dto.gender())
                .bloodGroup(dto.bloodGroup())
                .passportNumber(dto.passportNumber())
                .visaType(dto.visaType())
                .permanentAddress(dto.permanentAddress())
                .parentEmail(dto.parentEmail())
                .parentPhone(dto.parentPhone())
                .emergencyContact(dto.emergencyContact())
                .build();
    }

    /**
     * Internal helper to synchronize fields from a DTO to an existing Entity.
     * * @param entity The target entity to be updated
     * @param dto The source data transfer object
     * @since 1.0
     */
    private void updateEntityFields(StudentDemographics entity, DemographicsRequestDTO dto) {
        entity.setLegalFullName(dto.legalFullName());
        entity.setNationality(dto.nationality());
        entity.setReligion(dto.religion());
        entity.setGender(dto.gender());
        entity.setBloodGroup(dto.bloodGroup());
        entity.setPassportNumber(dto.passportNumber());
        entity.setVisaType(dto.visaType());
        entity.setPermanentAddress(dto.permanentAddress());
        entity.setParentEmail(dto.parentEmail());
        entity.setParentPhone(dto.parentPhone());
        entity.setEmergencyContact(dto.emergencyContact());
    }
}