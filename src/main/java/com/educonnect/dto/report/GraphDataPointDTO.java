package com.educonnect.dto.report;

import java.time.LocalDate;

public record GraphDataPointDTO(
        LocalDate date,
        Double grade
) {}