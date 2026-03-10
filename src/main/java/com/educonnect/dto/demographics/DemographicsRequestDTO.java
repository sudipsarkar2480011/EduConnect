package com.educonnect.dto.demographics;

import com.educonnect.model.demographics.embeddables.Address;
import com.educonnect.model.demographics.embeddables.EmergencyContact;
import com.educonnect.model.demographics.enums.BloodGroup;
import com.educonnect.model.demographics.enums.Gender;
import com.educonnect.model.demographics.enums.VisaType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Captures demographic data from the frontend during registration or profile update.
 */
public record DemographicsRequestDTO(
        @NotBlank(message = "Legal name is required") String legalFullName,
        @NotBlank(message = "Nationality is required") String nationality,
        String religion,
        @NotNull(message = "Gender is required") Gender gender,
        BloodGroup bloodGroup,
        String passportNumber,
        VisaType visaType,
        @NotNull(message = "Permanent address is required") Address permanentAddress,
        @Email(message = "Valid parent email is required") @NotBlank String parentEmail,
        @NotBlank(message = "Parent phone is required") String parentPhone,
        @NotNull(message = "Emergency contact is required") EmergencyContact emergencyContact
) {}