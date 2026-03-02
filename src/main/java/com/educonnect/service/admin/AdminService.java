package com.educonnect.service.admin;

import com.educonnect.model.user.Admin;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    Admin create(Admin admin);
    Admin getById(UUID id);
    List<Admin> getAll();
    Admin update(UUID id, Admin admin);
    void changePassword(UUID id, String newRawPassword);
    void delete(UUID id);
}
