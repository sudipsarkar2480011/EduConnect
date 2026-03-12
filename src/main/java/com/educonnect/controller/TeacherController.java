package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.dto.course.CourseRequestDTO;
import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.teacher.TeacherCreateDTO;
import com.educonnect.dto.teacher.TeacherResponseDTO;
import com.educonnect.dto.teacher.TeacherUpdateDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.service.contract.TeacherService;
import com.educonnect.service.contract.course.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/api/teachers")
@RequiredArgsConstructor
@Tag(name = "05 TeacherController")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("test")
    public  String test(){
        return "working";
    }


    @GetMapping("/{id}")
    public TeacherResponseDTO getById(@PathVariable UUID id) throws UserNotFoundException {
        return teacherService.getById(id);
    }

    @GetMapping
    public Page<TeacherResponseDTO> getAll(Pageable pageable) {
        return teacherService.getAll(pageable);
    }

    @PatchMapping("/{id}")
    public TeacherResponseDTO update(@PathVariable UUID id, @RequestBody TeacherUpdateDTO dto) throws UserNotFoundException {
        return teacherService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) throws UserNotFoundException {
        teacherService.delete(id);
    }


}
