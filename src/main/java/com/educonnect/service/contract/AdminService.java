package com.educonnect.service.contract;

import com.educonnect.model.user.Admin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdminService {
        Optional<Admin> getById(UUID id);
        List<Admin> getAll();
        Admin update(UUID id, Admin admin);
        void delete(UUID id);

}
