package com.educonnect.dto.report;

public record AttendanceStatsDTO(
long totalPresent,
long totalAbsent,
double averageAttendanceRate
) {}