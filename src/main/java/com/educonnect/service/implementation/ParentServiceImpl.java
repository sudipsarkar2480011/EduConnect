package com.educonnect.service.implementation;

import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.model.user.Parent;
import com.educonnect.repo.ParentRepo;
import com.educonnect.service.contract.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParentServiceImpl implements ParentService {

    @Autowired
    private ParentRepo parentRepo;

    @Override
    public Parent getById(UUID id) {
        return parentRepo.getById(id);
    }

    @Override
    public List<Parent> getAll() {
        return parentRepo.findAll();
    }

    @Override
    public ParentResponseDTO update(UUID id, ParentUpdateDTO dto) {
        return ParentResponseDTO.builder()
                .contactInfo(dto.getContactInfo())
                .name(dto.getName())
                .build();
    }

    @Override
    public void delete(UUID id) {
        parentRepo.delete(parentRepo.getById(id));

    }

    @Override
    public ParentResponseDTO linkStudent(UUID parentId, UUID studentId) {
        return null;
    }
}
