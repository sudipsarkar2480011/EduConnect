package com.educonnect.controller;

import com.educonnect.dto.parent.ParentCreateDTO;
import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.service.contract.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParentResponseDTO create(@RequestBody ParentCreateDTO dto) {
        return parentService.create(dto);
    }

    @GetMapping("/{id}")
    public ParentResponseDTO getById(@PathVariable UUID id) {
        return parentService.getById(id);
    }

    @GetMapping
    public Page<ParentResponseDTO> getAll(Pageable pageable) {
        return parentService.getAll(pageable);
    }

    @PatchMapping("/{id}")
    public ParentResponseDTO update(@PathVariable UUID id, @RequestBody ParentUpdateDTO dto) {
        return parentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        parentService.delete(id);
    }
}