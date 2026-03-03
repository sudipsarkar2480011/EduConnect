package com.educonnect.controller;

import com.educonnect.dto.teacher.TeacherCreateDTO;
import com.educonnect.dto.teacher.TeacherResponseDTO;
import com.educonnect.dto.teacher.TeacherUpdateDTO;
import com.educonnect.service.contract.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("test")
    public  String test(){
        return "working";
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
    public ResponseEntity<String> deleteTeacher(@PathVariable UUID id)
    {
        teacherService.delete(id);
        return ResponseEntity.ok("teacher Deleted : ");
    }
}
