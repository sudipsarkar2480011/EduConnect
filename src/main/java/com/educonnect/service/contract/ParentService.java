package com.educonnect.service.contract;

import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.model.user.Parent;

import java.util.List;
import java.util.UUID;

public interface ParentService {

    public Parent getById(UUID id);

    public List<Parent> getAll();

    public ParentResponseDTO update(UUID id, ParentUpdateDTO dto);

    public void delete(UUID id);

    public ParentResponseDTO linkStudent(UUID parentId, UUID studentId);
}
