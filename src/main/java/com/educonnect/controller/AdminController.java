package com.educonnect.controller;

import com.educonnect.model.user.Admin;
import com.educonnect.model.user.Role;
import com.educonnect.service.contract.AdminService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/admin")
public class AdminController {


    @Autowired
    private AdminService adminService;

    @GetMapping("/{id}")
    public Optional<Admin> getById(@PathVariable UUID id) {
        return adminService.getById(id);
    }


    @GetMapping
    public List<Admin> getAll() {
        return adminService.getAll();
    }

    @PutMapping("/{id}")
    public Admin update(@PathVariable UUID id, @RequestBody Admin admin) {
        return adminService.update(id, admin);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        adminService.delete(id);
    }
}