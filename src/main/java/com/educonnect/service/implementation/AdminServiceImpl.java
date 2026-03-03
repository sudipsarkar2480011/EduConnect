package com.educonnect.service.implementation;

import com.educonnect.model.user.Admin;
import com.educonnect.repo.AdminRepo;
import com.educonnect.service.contract.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepo adminRepo;
    @Override
    public Optional<Admin> getById(UUID id) {
       return adminRepo.findById(id);
    }

    @Override
    public List<Admin> getAll() {
        return adminRepo.findAll();
    }

    @Override
    public Admin update(UUID id, Admin admin) {
        return adminRepo.save(admin);
    }

    @Override
    public void delete(UUID id) {
        adminRepo.deleteById(id);
    }
}
