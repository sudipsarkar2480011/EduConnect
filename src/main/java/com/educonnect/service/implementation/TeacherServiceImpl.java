package com.educonnect.service.implementation;

import com.educonnect.dto.teacher.TeacherCreateDTO;
import com.educonnect.dto.teacher.TeacherResponseDTO;
import com.educonnect.dto.teacher.TeacherUpdateDTO;
import com.educonnect.model.user.Teacher;
import com.educonnect.repo.TeacherRepo;
import com.educonnect.service.contract.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepo teacherRepo;

    @Autowired
    public TeacherServiceImpl(TeacherRepo teacherRepo) {
        this.teacherRepo = teacherRepo;
    }


    @Override
    public TeacherResponseDTO getById(UUID id) {
        Teacher t = teacherRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found: " + id));
        return toResponse(t);
    }

    @Override
    public Page<TeacherResponseDTO> getAll(Pageable pageable) {
        return teacherRepo.findAll(pageable).map(this::toResponse);
    }

    @Override
    public TeacherResponseDTO update(UUID id, TeacherUpdateDTO dto) {
        Teacher t = teacherRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found: " + id));

        if (dto.getFullName() != null)      t.setFullName(dto.getFullName());
        if (dto.getEmail() != null)         t.setEmail(dto.getEmail());
        if (dto.getDepartment() != null)    t.setDepartment(dto.getDepartment());
        if (dto.getQualification() != null) t.setQualification(dto.getQualification());

        Teacher updated = teacherRepo.save(t);
        return toResponse(updated);
    }

    @Override
    public void delete(UUID id) {
        if (!teacherRepo.existsById(id)) {
            throw new RuntimeException("Teacher not found: " + id);
        }
        teacherRepo.deleteById(id);
    }

    private TeacherResponseDTO toResponse(Teacher t) {
        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(t.getUserId());
        dto.setFullName(t.getFullName());
        dto.setEmail(t.getEmail());
        dto.setDepartment(t.getDepartment());
        dto.setQualification(t.getQualification());
        return dto;
    }


}


