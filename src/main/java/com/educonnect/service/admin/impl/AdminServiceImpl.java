package com.educonnect.service.admin.impl;

import com.educonnect.model.user.Admin;
import com.educonnect.model.user.Role;
import com.educonnect.repo.AdminRepo;
import com.educonnect.service.admin.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminRepo adminRepo;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Admin create(Admin incoming) {
        Admin toSave = Admin.builder()
                .fullName(incoming.getFullName())
                .email(incoming.getEmail())
                .password(encoder.encode(incoming.getPassword()))
                .role(Role.ADMIN)
                .build();

        return adminRepo.save(toSave);
    }

    @Override
    @Transactional(readOnly = true)
    public Admin getById(UUID id) {
        return adminRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Admin> getAll() {
        return adminRepo.findAll();
    }

    @Override
    public Admin update(UUID id, Admin incoming) {
        Admin existing = getById(id);

        // update allowed fields (avoid null overwrites unless provided)
        if (incoming.getFullName() != null) existing.setFullName(incoming.getFullName());
        if (incoming.getEmail() != null) existing.setEmail(incoming.getEmail());

        // keep role as ADMIN
        existing.setRole(Role.ADMIN);

        // If password provided in update, re-encode it
        if (incoming.getPassword() != null && !incoming.getPassword().isBlank()) {
            existing.setPassword(encoder.encode(incoming.getPassword()));
        }

        return adminRepo.save(existing);
    }

    @Override
    public void changePassword(UUID id, String newRawPassword) {
        Admin admin = getById(id);
        admin.setPassword(encoder.encode(newRawPassword));
        adminRepo.save(admin);
    }

    @Override
    public void delete(UUID id) {
        if (!adminRepo.existsById(id)) {
            throw new EntityNotFoundException("Admin not found with id: " + id);
        }
        adminRepo.deleteById(id);
    }
}