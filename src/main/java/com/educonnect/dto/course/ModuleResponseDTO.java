package com.educonnect.dto.course;

import java.util.List;
import java.util.UUID;

public record  ModuleResponseDTO (
       String contentUrl,
       UUID moduleId,
       String title,
       Double duration,
       Integer sequenceOrder

){
}
