package com.educonnect.dto.course;

import com.educonnect.model.course.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

public record ModuleRequestDTO (
        UUID courseId,
        String courseName
) {


}
