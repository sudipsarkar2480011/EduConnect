package com.educonnect.controller;
import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.model.user.Parent;
import com.educonnect.service.contract.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/api/parents")
@RequiredArgsConstructor
public class ParentController {


    private final ParentService parentService;

    @GetMapping("/{id}")
    public Parent getById(@PathVariable UUID id) {
        return parentService.getById(id);
    }

    @GetMapping
    public List<Parent> getAllParentList() {
        return parentService.getAll();
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