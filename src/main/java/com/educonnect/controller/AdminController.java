package com.educonnect.controller;

import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Admin;
import com.educonnect.model.user.Role;
import com.educonnect.service.contract.AdminService;
import com.educonnect.service.contract.ParentService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private final AdminService adminService;

    @GetMapping("/{id}")
    public Admin getById(@PathVariable UUID id) throws UserNotFoundException {
        return adminService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Admin admin) throws UserNotFoundException {
        adminService.update(id, admin);
        return new ResponseEntity<>("User updated successfully: ",HttpStatus.ACCEPTED);
    }


    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String>  delete(@PathVariable UUID id) throws UserNotFoundException {
        adminService.delete(id);
        return new ResponseEntity<>("User deleted Successfully: ",HttpStatus.OK);
    }


}