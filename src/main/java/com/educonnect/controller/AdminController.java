package com.educonnect.controller;

import com.educonnect.model.user.Admin;
import com.educonnect.model.user.Role;
import com.educonnect.service.admin.AdminService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Admin management endpoints (no registration or login here).
 * Uses Admin entity directly.
 *
 * @author abramya975
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/admin")
public class AdminController {

    private final AdminService adminService;

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Admin create(@RequestBody Admin admin) {
        // Role is enforced in service; setting here is optional but explicit
        admin.setRole(Role.ADMIN);
        return adminService.create(admin);
    }

    // READ (by id)
    @GetMapping("/{id}")
    public Admin getById(@PathVariable UUID id) {
        return adminService.getById(id);
    }

    // LIST
    @GetMapping
    public List<Admin> getAll() {
        return adminService.getAll();
    }

    // UPDATE (full or partial—fields that are null won't overwrite)
    @PutMapping("/{id}")
    public Admin update(@PathVariable UUID id, @RequestBody Admin admin) {
        // Any password provided here will be encoded by the service
        admin.setRole(Role.ADMIN);
        return adminService.update(id, admin);
    }

    // CHANGE PASSWORD (minimal body, no DTO used)
    @PatchMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@PathVariable UUID id,
                               @RequestBody PasswordOnly body) {
        adminService.changePassword(id, body.password());
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        adminService.delete(id);
    }

    // Simple inner class for password-only PATCH body
    public record PasswordOnly(@NotBlank String password) {}
}