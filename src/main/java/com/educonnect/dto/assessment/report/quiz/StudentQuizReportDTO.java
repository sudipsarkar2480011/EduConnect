package com.educonnect.dto.assessment.report.quiz;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class StudentQuizReportDTO {
    private UUID submissionId;
    List<StudentQuestionAttemptDTO> studentQuestionAttemptDTOList;
}
