package com.educonnect.service.contract;


import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Admin;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    Admin getById(UUID id) throws UserNotFoundException;
    List<Admin> getAll();
    Admin update(UUID id, Admin admin) throws UserNotFoundException;
    void delete(UUID id) throws UserNotFoundException;
}
