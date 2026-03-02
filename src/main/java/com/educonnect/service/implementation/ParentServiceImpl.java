package com.educonnect.service.implementation;

import com.educonnect.dto.parent.ParentCreateDTO;
import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Student;
import com.educonnect.repo.ParentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.ParentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepo parentRepo;
    private final StudentRepo studentRepo;

    @Autowired
    public ParentServiceImpl(ParentRepo parentRepo, StudentRepo studentRepo) {
        this.parentRepo = parentRepo;
        this.studentRepo = studentRepo;
    }

    @Override
    @Transactional
    public ParentResponseDTO create(ParentCreateDTO dto) {
        Parent p = new Parent();
        // Map to base User fields (using fullName as per Teacher example)
        p.setFullName(dto.getName());
        // Optional: if your User has email/username requirements, add them in create DTO & set here.

        // Parent-specific fields
        p.setContactInfo(dto.getContactInfo());


        Parent saved = parentRepo.save(p);

        // Handle initial linking of students if provided
        if (dto.getLinkedStudentIds() != null && !dto.getLinkedStudentIds().isEmpty()) {
            List<Student> students = studentRepo.findAllById(dto.getLinkedStudentIds());

            // Validate all requested students exist
            Set<UUID> foundIds = students.stream()
                    .map(Student::getUserId)
                    .collect(Collectors.toSet());

            List<UUID> missing = dto.getLinkedStudentIds().stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            if (!missing.isEmpty()) {
                throw new RuntimeException("Some students not found: " + missing);
            }

            // Set parent for each student (owning side)
            for (Student s : students) {
                s.setParent(saved);
            }
            studentRepo.saveAll(students);
        }

        // Optionally refresh from DB if you need populated linkedStudents list
        Parent persisted = parentRepo.findById(saved.getUserId())
                .orElseThrow(() -> new RuntimeException("Parent not found after save: " + saved.getUserId()));

        return toResponse(persisted);
    }

    @Override
    public ParentResponseDTO getById(UUID id) {
        Parent p = parentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found: " + id));
        return toResponse(p);
    }

    @Override
    public Page<ParentResponseDTO> getAll(Pageable pageable) {
        return parentRepo.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional
    public ParentResponseDTO update(UUID id, ParentUpdateDTO dto) {
        Parent p = parentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found: " + id));

        // Update scalar fields
        if (dto.getName() != null)        p.setFullName(dto.getName());
        if (dto.getContactInfo() != null) p.setContactInfo(dto.getContactInfo());


        // Update linked students if provided
        if (dto.getLinkedStudentIds() != null) {
            // Current links
            List<Student> currentlyLinked = Optional.ofNullable(p.getLinkedStudents())
                    .orElseGet(List::of);

            Set<UUID> newIds = new HashSet<>(dto.getLinkedStudentIds());
            Set<UUID> currentIds = currentlyLinked.stream()
                    .map(Student::getUserId)
                    .collect(Collectors.toSet());

            // Determine removals and additions
            Set<UUID> toRemove = new HashSet<>(currentIds);
            toRemove.removeAll(newIds);

            Set<UUID> toAdd = new HashSet<>(newIds);
            toAdd.removeAll(currentIds);

            // Unlink students to remove
            if (!toRemove.isEmpty()) {
                List<Student> removeList = studentRepo.findAllById(toRemove);
                for (Student s : removeList) {
                    s.setParent(null);
                }
                studentRepo.saveAll(removeList);
            }

            // Link students to add
            if (!toAdd.isEmpty()) {
                List<Student> addList = studentRepo.findAllById(toAdd);

                Set<UUID> foundAddIds = addList.stream()
                        .map(Student::getUserId)
                        .collect(Collectors.toSet());

                List<UUID> missingAdd = toAdd.stream()
                        .filter(id2 -> !foundAddIds.contains(id2))
                        .toList();

                if (!missingAdd.isEmpty()) {
                    throw new RuntimeException("Some students not found: " + missingAdd);
                }

                for (Student s : addList) {
                    s.setParent(p);
                }
                studentRepo.saveAll(addList);
            }
        }

        Parent updated = parentRepo.save(p);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Parent p = parentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found: " + id));

        // Unlink students first to avoid FK constraint issues
        List<Student> linked = Optional.ofNullable(p.getLinkedStudents()).orElseGet(List::of);
        if (!linked.isEmpty()) {
            for (Student s : linked) {
                s.setParent(null);
            }
            studentRepo.saveAll(linked);
        }

        parentRepo.deleteById(id);
    }

    @Override
    @Transactional
    public ParentResponseDTO linkStudent(UUID parentId, UUID studentId) {
        Parent p = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found: " + parentId));

        Student s = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        s.setParent(p);
        studentRepo.save(s);

        // Optionally refresh to ensure linkedStudents reflects the new link
        Parent refreshed = parentRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found after linking: " + parentId));

        return toResponse(refreshed);
    }

    // -----------------------------
    // Mapper
    // -----------------------------
    private ParentResponseDTO toResponse(Parent p) {
        ParentResponseDTO dto = new ParentResponseDTO();
        dto.setId(p.getUserId());
        dto.setName(p.getFullName());
        dto.setContactInfo(p.getContactInfo());
        dto.setStatus(p.isActive());

        List<Student> children = Optional.ofNullable(p.getLinkedStudents())
                .orElseGet(List::of);

        dto.setLinkedStudentIds(
                children.stream()
                        .map(Student::getUserId)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}