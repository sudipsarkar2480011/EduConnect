package com.educonnect.repo;

import com.educonnect.model.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepo extends JpaRepository<Admin, UUID> {
}
