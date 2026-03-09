package com.educonnect.dto.course;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CourseResponseDTO (
     @NotBlank   UUID courseId,
    @NotBlank
    String title,
    @NotBlank String description,
    @NotBlank  String courseCode,
    @NotBlank   Double duration,
    @NotBlank
    UUID teacherId,
    String teacherName
){
}