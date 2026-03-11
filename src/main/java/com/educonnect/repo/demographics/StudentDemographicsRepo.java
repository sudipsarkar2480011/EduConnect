package com.educonnect.repo.demographics;

import com.educonnect.model.demographics.StudentDemographics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentDemographicsRepo extends JpaRepository<StudentDemographics, UUID> {
    // Basic CRUD is handled automatically by Spring Data JPA
}