package com.educonnect.controller;

import com.educonnect.dto.TeacherCreateDTO;
import com.educonnect.dto.TeacherResponseDTO;
import com.educonnect.dto.TeacherUpdateDTO;
import com.educonnect.service.contract.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {


    @Autowired
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherResponseDTO create(@RequestBody TeacherCreateDTO dto) {
        return teacherService.create(dto);
    }

    @GetMapping("/{id}")
    public TeacherResponseDTO getById(@PathVariable UUID id) {
        return teacherService.getById(id);
    }

    @GetMapping
    public Page<TeacherResponseDTO> getAll(Pageable pageable) {
        return teacherService.getAll(pageable);
    }

    @PatchMapping("/{id}")
    public TeacherResponseDTO update(@PathVariable UUID id, @RequestBody TeacherUpdateDTO dto) {
        return teacherService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        teacherService.delete(id);
    }

}
