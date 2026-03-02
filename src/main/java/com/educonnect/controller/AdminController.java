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
 * <p><b>Base Path:</b> /v1/api/admin</p>
 *
 * <h2>Quick Start (cURL)</h2>
 *
 * <p><b>Create Admin</b></p>
 * <pre>
 * curl -X POST http://localhost:8081/v1/api/admin \
 *   -H "Content-Type: application/json" \
 *   -d '{
 *         "fullName": "Jane Admin",
 *         "email": "jane@school.edu",
 *         "password": "StrongPass#123"
 *       }'
 * </pre>
 * <p><b>Get Admin by ID</b></p>
 * <pre>
 * curl http://localhost:8080/v1/api/admin/{id}
 * </pre>
 *
 * <p><b>List Admins</b></p>
 * <pre>
 * curl http://localhost:8081/v1/api/admin
 * </pre>
 *
 * <p><b>Update Admin</b></p>
 * <pre>
 * curl -X PUT http://localhost:8080/v1/api/admin/{id} \
 *   -H "Content-Type: application/json" \
 *   -d '{
 *         "fullName": "Jane A. Admin",
 *         "email": "jane.admin@school.edu",
 *         "password": "OptionalNew#456"
 *       }'
 * </pre>
 *
 * <p><b>Change Password</b></p>
 * <pre>
 * curl -X PATCH http://localhost:8080/v1/api/admin/{id}/password \
 *   -H "Content-Type: application/json" \
 *   -d '{ "password": "NewStrong#789" }'
 * </pre>
 *
 * <p><b>Delete Admin</b></p>
 * <pre>
 * curl -X DELETE http://localhost:8080/v1/api/admin/{id}
 * </pre>
 *
 * <p><b>Notes</b></p>
 * <ul>
 *   <li>Password is always hashed server-side.</li>
 *   <li>Role is enforced as ADMIN in the service layer.</li>
 *   <li>Uses UUID as the primary key (path variable).</li>
 *   <li>Consider adding @JsonIgnore on password in the entity to avoid exposing it in responses.</li>
 * </ul>
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