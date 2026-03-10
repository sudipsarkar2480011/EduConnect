package com.educonnect.repo;

import com.educonnect.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link Student} entity operations.
 * <p>Extends {@link JpaRepository} to provide standard CRUD functionality
 * and custom query methods for student data management.</p>
 *
 * @author sudip sarkar
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface StudentRepo extends JpaRepository<Student, UUID> {

    /**
     * Retrieves a student profile based on their unique User identifier.
     *
     * @param studentId the UUID associated with the student's account.
     * @return an {@link Optional} containing the student if found, or empty if not.
     */
    Optional<Student> findByUserId(UUID studentId);
    /**
     * Checks for the existence of a student record in the database.
     *
     * @param StudentId the UUID to check against existing records.
     * @return {@code true} if a student exists with the given ID; {@code false} otherwise.
     */
    boolean existsByUserId(UUID StudentId);

}
