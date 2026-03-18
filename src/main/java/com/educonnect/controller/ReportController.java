package com.educonnect.controller;

import com.educonnect.dto.report.AttendanceStatsDTO;
import com.educonnect.dto.report.ExamStatsDTO;
import com.educonnect.dto.report.FullSystemReportDTO;
import com.educonnect.dto.report.GraphDataPointDTO;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.service.implementation.report.ReportServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for generating administrative and system-wide reports.
 */
@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
@Tag(name = "03 ReportController")
public class ReportController {

    private final ReportServiceImpl reportService;

    /**
     * Retrieves a comprehensive report of all system data.
     * @return A DTO containing aggregated system statistics.
     */
    @GetMapping("/system-summary")
    public ResponseEntity<FullSystemReportDTO> getFullReport() {
        return ResponseEntity.ok(reportService.getAllDataReport());
    }

    /**
     * Retrieves a specialized report for all students.
     * MOVED: Transferred from StudentController to consolidate reporting logic.
     */
    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getAllStudentsReport() {
        return ResponseEntity.ok(reportService.getAllStudents());
    }


    //attendance statistics, exam statistics,
    @GetMapping("/stats/attendance")
    public ResponseEntity<AttendanceStatsDTO> getAttendanceStats() {
        return ResponseEntity.ok(reportService.getAttendanceStatistics());
    }

    @GetMapping("/stats/exams")
    public ResponseEntity<ExamStatsDTO> getExamStats() {
        return ResponseEntity.ok(reportService.getExamStatistics());
    }

    @GetMapping("/student/{studentId}/performance-trend")
    public ResponseEntity<List<GraphDataPointDTO>> getStudentTrend(@PathVariable UUID studentId) {
        // This serves Parents, Students, and Admins the same data
        return ResponseEntity.ok(reportService.getStudentPerformanceTrend(studentId));
    }

    @GetMapping("/course/{courseId}/performance-trend")
    public ResponseEntity<List<GraphDataPointDTO>> getCourseTrend(@PathVariable UUID courseId) {
        return ResponseEntity.ok(reportService.getCoursePerformanceTrend(courseId));
    }
}