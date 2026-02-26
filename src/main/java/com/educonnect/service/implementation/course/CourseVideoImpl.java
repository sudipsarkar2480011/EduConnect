package com.educonnect.service.implementation.course;

import com.educonnect.model.course.Course;
import com.educonnect.model.course.CourseModule;
import com.educonnect.repo.course.CourseModuleRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.contract.course.CourseVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;


@Service
public class CourseVideoImpl implements CourseVideoService {

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

        Optional.ofNullable(file).orElseThrow(()->new RuntimeException("File not found : "));

       Optional.of(file).filter(f-> f.getContentType().startsWith("video/"))
               .orElseThrow(()->new RuntimeException("file is not a video : "));

        String extension = file.getOriginalFilename()
                .substring(file.getOriginalFilename()
                        .lastIndexOf("."));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IOException("Course does not exist"));

        File directory = new File(uploadDir);

        if (!directory.exists()) directory.mkdirs();

        UUID uuid = UUID.randomUUID();

        String filename = uuid.toString() + extension;

        Path filePath = Paths.get(uploadDir).resolve(filename);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return courseModuleRepoRepo.save(CourseModule.builder()
                .moduleId(uuid)
                .sequenceOrder(sequenceOrder)
                .contentUrl(filename)
                .course(course)
                .title(title)
                .build());
    }

    @Override
    public String getVideoUrl(UUID id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/course/stream/")
                .path(id.toString())
                .toUriString();
    }

    @Override
    public Resource LoadVideoAsResource(UUID moduleId) throws IOException {
        CourseModule video = courseModuleRepoRepo.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Video record not found"));

        Path path = Paths.get(uploadDir).resolve(video.getContentUrl()).normalize();

        if (!Files.exists(path)) throw new RuntimeException("File not found on disk");

        return new UrlResource(path.toUri());
    }
}
