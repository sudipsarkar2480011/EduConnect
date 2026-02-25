package com.educonnect.service.course;

import com.educonnect.model.course.Course;
import com.educonnect.model.course.CourseModule;
import com.educonnect.repo.course.CourseModuleRepo;
import com.educonnect.repo.course.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;


@Service
public class CourseVideoImpl implements CourseVideoService{

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private CourseModuleRepo courseModuleRepoRepo;

    @Autowired
    private CourseRepo courseRepo;
    @Override
    public CourseModule uploadVideo(
            MultipartFile file,
            String title,
            Integer sequenceOrder,
            UUID courseId) throws IOException {

        if(file.isEmpty())
        {
            throw new RuntimeException("file is empty: ");
        }

        if (!file.getContentType().startsWith("video/")) {
            throw new IllegalArgumentException("Only video files are allowed");
        }
        Course course=courseRepo.findById(courseId).orElseThrow(()->new IOException("course donot exists: "));
       File directory=new File(uploadDir);
       if(!directory.exists())
       {
           directory.mkdirs();
       }

       String filename= UUID.randomUUID()+"_"+file.getOriginalFilename();

        Path filePath= Paths.get(uploadDir).resolve(filename);
        Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);
        return courseModuleRepoRepo.save(CourseModule
                .builder()
                .sequenceOrder(sequenceOrder)
                .contentUrl(filename)
                .course(course)
                .title(title)
                .build());
    }

    @Override
    public Resource getVideo(UUID videoId) throws IOException {
        CourseModule video=courseModuleRepoRepo.findById(videoId)
                .orElseThrow(()->new IOException("no video found: "));
        Path path= Paths.get(uploadDir).resolve(video.getContentUrl());
        Resource resource=new UrlResource(path.toUri());
        if(!resource.exists())
        {
            throw new IOException("file not found: ");
        }
        return resource;
    }
}
