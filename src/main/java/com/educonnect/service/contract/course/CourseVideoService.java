package com.educonnect.service.contract.course;

import com.educonnect.exception.custom_exceptions.UserIdDoNothMatchException;
import com.educonnect.model.course.Course;
import com.educonnect.model.course.CourseModule;
import com.educonnect.model.user.Student;
import org.jspecify.annotations.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public interface CourseVideoService {
    CourseModule uploadVideo(MultipartFile file, String title, Integer sequenceOrder, UUID courseId) throws IOException, EncoderException;
    String getVideoUrl(UUID id) throws IOException;
    Resource LoadVideoAsResource(UUID id) throws IOException;
    String deleteVideoResource(CourseModule video, Course course) throws IOException;
    CourseModule updateVideoResource(MultipartFile file,String title,UUID videoId, UUID courseId , UUID userId) throws IOException, EncoderException;
    Map<String, String> markModuleAsCompleted(UUID moduleId, UUID courseId, Student student);String deleteVideoResourceWithids(UUID videoId, UUID courseId, UUID id) throws IOException, UserIdDoNothMatchException;
}
