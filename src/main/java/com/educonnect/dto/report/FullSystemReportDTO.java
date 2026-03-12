package com.educonnect.dto.report;

import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.teacher.TeacherResponseDTO;
import com.educonnect.dto.user.UserResponseDTO;
import com.educonnect.model.report.Report;
import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Admin;
import java.util.List;

public record FullSystemReportDTO(
        List<StudentResponse> students,
        List<TeacherResponseDTO> teachers,
        List<Parent> parents,
        List<Admin> admins,
        List<UserResponseDTO> allUsers,
        List<CourseResponseDTO> courses,
        List<String> roles,
        List<Report> generatedReports
) {}