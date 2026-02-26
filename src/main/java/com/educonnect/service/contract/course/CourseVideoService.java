package com.educonnect.service.contract.course;

import com.educonnect.model.course.CourseModule;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public interface CourseVideoService {
    CourseModule uploadVideo(MultipartFile file, String title, Integer sequenceOrder, UUID courseId) throws IOException;
    String getVideoUrl(UUID id) throws IOException;
    Resource LoadVideoAsResource(UUID id) throws IOException;
}
