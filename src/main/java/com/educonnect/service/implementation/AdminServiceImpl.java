package com.educonnect.service.implementation;

import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Admin;
import com.educonnect.repo.AdminRepo;
import com.educonnect.service.contract.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Override
    public Admin getById(UUID id) throws UserNotFoundException {
        return adminRepo.findById(id).orElseThrow(()-> new UserNotFoundException("User Not Found : "));
    }

    @Override
    public List<Admin> getAll() {
        return adminRepo.findAll();
    }

    @Override
    public Admin update(UUID id, Admin admin) throws UserNotFoundException {
        Admin admin1=adminRepo.findById(id).orElseThrow(()-> new UserNotFoundException("User don't exists : "));
        admin1.builder()
                .email(admin.getEmail())
                .password(admin.getPassword())
                .fullName(admin.getFullName())
                .build();
        return admin1;
    }

    @Override
    public void delete(UUID id) throws UserNotFoundException {

        adminRepo.deleteById(id);
    }
}
