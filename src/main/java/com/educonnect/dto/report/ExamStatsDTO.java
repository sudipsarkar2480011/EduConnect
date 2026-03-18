package com.educonnect.dto.report;

public record ExamStatsDTO(
        double averageScore,
        long totalExamsTaken,
        double highestScore
) {}