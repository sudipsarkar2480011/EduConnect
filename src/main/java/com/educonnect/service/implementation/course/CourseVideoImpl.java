package com.educonnect.service.implementation.course;

import com.educonnect.model.course.Course;
import com.educonnect.model.course.CourseModule;
import com.educonnect.repo.course.CourseModuleRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.contract.course.CourseVideoService;
import com.educonnect.utils.video.VideoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class CourseVideoImpl implements CourseVideoService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.tempUpload-dir}")
    private String tempUploadDir;

    @Autowired
    private CourseModuleRepo courseModuleRepo;

    @Autowired
    private CourseRepo courseRepo;


    @Override
    public CourseModule uploadVideo(
            MultipartFile file,
            String title,
            Integer sequenceOrder,
            UUID courseId) throws IOException, EncoderException {

        File tempFile = null;
        try{
           Optional.ofNullable(file).orElseThrow(()->new RuntimeException("File not found : "));

           Optional.of(file).filter(f-> f.getContentType().startsWith("video/"))
                   .orElseThrow(()->new RuntimeException("file is not a video : "));

           String extension = file.getOriginalFilename()
                   .substring(file.getOriginalFilename()
                           .lastIndexOf("."));

           Course course = courseRepo.findById(courseId)
                   .orElseThrow(() -> new IOException("Course does not exist"));

           File directory = new File(uploadDir);
           File tempDirectory = new File(tempUploadDir);

           UUID uuid = UUID.randomUUID();

           String filename = uuid.toString()  + extension;

           if (!directory.exists()) directory.mkdirs();
           if(!tempDirectory.exists()) tempDirectory.mkdirs();

           Path filePath = Paths.get(uploadDir).resolve(filename);
           Path tempFilePath = Files.createTempFile(tempDirectory.toPath(),"temp-video-" + uuid ,extension);

           Files.copy(file.getInputStream(), tempFilePath , StandardCopyOption.REPLACE_EXISTING);
           tempFile = tempFilePath.toFile();

           if (!tempFile.exists() || tempFile.length() == 0) {
               throw new IOException("File was not written correctly to disk.");
           }

           double duration = VideoUtil.getVideoDuration(tempFile);

           Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
           var resp = courseModuleRepo.save(CourseModule.builder()
                   .moduleId(uuid)
                   .sequenceOrder(sequenceOrder)
                   .contentUrl(filename)
                   .course(course)
                   .title(title)
                   .duration(duration)
                   .build());


           log.info("course duration 01={}",course.getDuration());

           course.setDuration(
                   (course.getDuration() == null ? 0 : course.getDuration() )
                           + duration);

           courseRepo.save(course);


           log.info("course duration 02={}",course.getDuration());

           return resp;

       }catch (Exception e){
           log.error(e.getMessage());
           throw e;
       }finally {
            System.out.println("finally");
            if(tempFile != null) {
                Files.deleteIfExists(tempFile.toPath());
            }

       }
    }

    @Override
    public String getVideoUrl(UUID id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/v1/api/course/stream/")
                .path(id.toString())
                .toUriString();
    }

    @Override
    public Resource LoadVideoAsResource(UUID moduleId) throws IOException {
        CourseModule video = courseModuleRepo.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Video record not found"));

        Path path = Paths.get(uploadDir).resolve(video.getContentUrl()).normalize();

        if (!Files.exists(path)) throw new RuntimeException("File not found on disk");

        return new UrlResource(path.toUri());
    }
}
