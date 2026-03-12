package com.educonnect.service.contract.course;

import com.educonnect.exception.custom_exceptions.UserIdDoNothMatchException;
import com.educonnect.model.course.CourseModule;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.service.contract.course.CourseVideoService;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.util.UUID;

public interface CourseVideoInerface  {
    CourseModule uploadVideo(MultipartFile file, String title, UUID courseId , UUID teacherId) throws IOException, EncoderException, UserIdDoNothMatchException;
}
