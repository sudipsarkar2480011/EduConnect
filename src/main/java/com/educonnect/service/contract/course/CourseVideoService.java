package com.educonnect.service.contract.course;

import com.educonnect.model.course.CourseModule;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface CourseVideoService {
    CourseModule uploadVideo(MultipartFile file, String title, Integer sequenceOrder, UUID courseId) throws IOException;
    Resource getVideo(UUID videoId) throws IOException;
}
