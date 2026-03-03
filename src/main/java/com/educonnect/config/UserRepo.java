package com.educonnect.config;

import com.educonnect.model.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import com.educonnect.model.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

}
