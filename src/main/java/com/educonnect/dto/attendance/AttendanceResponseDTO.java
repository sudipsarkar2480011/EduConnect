package com.educonnect.dto.attendance;

import com.educonnect.model.attendance.AttendanceStatus;

import java.time.LocalDate;
import java.util.UUID;

public record AttendanceResponseDTO(
        UUID attendanceId,
        UUID studentId,
        String studentEmail,
        UUID courseId,
        String courseTitle,
        LocalDate date,
        AttendanceStatus status
) {

}
