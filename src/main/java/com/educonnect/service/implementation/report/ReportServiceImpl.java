package com.educonnect.service.implementation.report;

import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.report.FullSystemReportDTO;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.teacher.TeacherResponseDTO;
import com.educonnect.dto.user.UserResponseDTO;
import com.educonnect.model.report.Report;
import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import com.educonnect.repo.AdminRepo;
import com.educonnect.repo.parent.ParentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.repo.TeacherRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.repo.report.ReportRepo; // Ensure you have this repo
import com.educonnect.config.UserRepo;
import com.educonnect.utils.mapper.CourseMapper;
import com.educonnect.utils.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl {

    private final StudentRepo studentRepo;
    private final TeacherRepo teacherRepo;
    private final ParentRepo parentRepo;
    private final AdminRepo adminRepo;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final ReportRepo reportRepo;

    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

    // 1. Fetch All Users (Base User Entity)
    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(this::mapToUserResponseDTO)
                .toList();
    }

    // 2. Fetch All Students
    public List<StudentResponse> getAllStudents() {
        return studentRepo.findAll()
                .stream()
                .map(studentMapper::toResponseDTO)
                .toList();
    }

    // 3. Fetch All Teachers
    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherRepo.findAll()
                .stream()
                .map(this::mapToTeacherResponseDTO)
                .toList();
    }

    // 4. Fetch All Courses (Referencing CourseServiceImpl logic)
    public List<CourseResponseDTO> getAllCourses() {
        return courseRepo.findAll()
                .stream()
                .map(courseMapper::toResponseDTO)
                .toList();
    }

    // 5. Fetch All Roles (From Enum)
    public List<String> getAllRoles() {
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .toList();
    }

    // 6. Fetch All Generated Reports
    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }

    public List<Parent> getAllParents() {
        return parentRepo.findAll();
    }

    // Comprehensive Report (Aggregated)
    public FullSystemReportDTO getAllDataReport() {
        return new FullSystemReportDTO(
                getAllStudents(),
                getAllTeachers(),
                parentRepo.findAll(),
                adminRepo.findAll(),
                getAllUsers(),
                getAllCourses(),
                getAllRoles(),
                getAllReports()
        );
    }

    // --- Helper Mappers ---

    private UserResponseDTO mapToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .uuid(user.getUserId())
                .email(user.getEmail())
                .name(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    private TeacherResponseDTO mapToTeacherResponseDTO(Teacher teacher) {
        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(teacher.getUserId());
        dto.setFullName(teacher.getFullName());
        dto.setEmail(teacher.getEmail());
        dto.setDepartment(teacher.getDepartment());
        dto.setQualification(teacher.getQualification());
        return dto;
    }
}