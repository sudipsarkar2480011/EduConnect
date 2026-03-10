package com.educonnect.service.implementation.course;

import com.educonnect.config.UserPrinciples;
import com.educonnect.exception.custom_exceptions.ResourceNotFoundException;
import com.educonnect.model.course.Course;
import com.educonnect.model.course.CourseModule;
import com.educonnect.model.course.Enrollment;
import com.educonnect.model.user.Student;
import com.educonnect.repo.EnrollmentRepo;
import com.educonnect.repo.course.CourseModuleRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.contract.course.CourseVideoService;
import com.educonnect.utils.video.VideoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
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

    @Autowired
    private EnrollmentRepo enrollmentRepo;


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

    @Override
    public String deleteVideoResourceWithids(UUID videoId, UUID courseId) throws IOException {
        CourseModule video = courseModuleRepo.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video record not found"));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));


        return deleteVideoResource(video,course);
    }

    @Override
    public String deleteVideoResource(CourseModule video, Course course) throws IOException {

        Path path = Paths.get(uploadDir).resolve(video.getContentUrl()).normalize();

        if(!Files.exists(path)){
            throw new ResourceNotFoundException("Video not found [on disk]");
        }

        video.setCourse(null);


        course.getModules().remove(video);

        course.setDuration(
                course.getDuration() != null? course.getDuration() - video.getDuration() : 0
        );

        courseRepo.save(course);

        log.info("video deleted successfully course table");
        System.out.println("video deleted successfully from course table");

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            throw new IOException(e);
        }

        courseModuleRepo.deleteById(video.getModuleId());
        return "Successfully deleted the video with title " + video.getTitle() + " of course with title " + course.getTitle();
    }

    @Override
    public CourseModule updateVideoResource(MultipartFile file, String title, UUID videoId, UUID courseId) throws IOException, EncoderException {

        Path tempFilePath = null;
        try{

            CourseModule video = courseModuleRepo.findById(videoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Video record not found"));

            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));


            Path path = Paths.get(uploadDir).resolve(video.getContentUrl()).normalize();

            Files.deleteIfExists(path);

            File directory = new File(uploadDir);
            File tempDirectory = new File(tempUploadDir);

            if(!directory.exists()) {
                directory.mkdirs();
            }
            if(!tempDirectory.exists()) {
                tempDirectory.mkdirs();
            }

            String extension = file.getOriginalFilename()
                    .substring(file.getOriginalFilename()
                            .lastIndexOf("."));

            String filename = video.getModuleId() + extension;

            Path filePath = Paths.get(uploadDir).resolve(filename);

            tempFilePath =
                    Files.createTempFile(
                            tempDirectory.toPath(),
                            "temp-video-" + video.getModuleId() ,
                            extension);

            Files.copy(file.getInputStream(), tempFilePath , StandardCopyOption.REPLACE_EXISTING);
            File tempFile = tempFilePath.toFile();
            if (!tempFile.exists() || tempFile.length() == 0) {
                throw new IOException("File was not written correctly to disk!");
            }

            var duration = VideoUtil.getVideoDuration(tempFile);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);



            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("duration "+ duration);
            System.out.println("video.getDuration() "+ video.getDuration());
            System.out.println("course.getDuration() "+ course.getDuration());
            System.out.println();
            System.out.println();;

            course.setDuration(
                    course.getDuration() != null ?
                            course.getDuration() - video.getDuration() + duration : 0
            );

            course.getModules().remove(video);

            video.setDuration(duration);
            video.setTitle(title);
            courseModuleRepo.save(video);

            courseRepo.save(course);

            log.info("video deleted successfully course table");
            System.out.println("video deleted successfully from course table");
            return video;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if(tempFilePath != null)
                Files.deleteIfExists(tempFilePath);
        }
    }


    @Override
    public Map<String, Object> markModuleAsCompleted(
            UUID moduleId,
            UUID courseId,
            Student student
            ){

        Enrollment enrollment =
                 enrollmentRepo.findByStudentUserIdAndCourseCourseId(student.getUserId(),courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrolment not found"));

        CourseModule courseModule = courseModuleRepo.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Course Module not found"));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        double gap = enrollment.getRemainingDuration() - courseModule.getDuration();

        enrollment.setRemainingDuration(gap < 0 ? 0 : gap);

        double progress = course.getDuration() <= 0 ?
                0 : (course.getDuration() - enrollment.getRemainingDuration()) / course.getDuration();

        enrollment.setProgress(progress);

        Map<String, Object> map = new HashMap<>();

        enrollmentRepo.save(enrollment);

        map.put("message",course.getTitle() + " is marked as completed");
        map.put("remainingTime",enrollment.getRemainingDuration());
        map.put("progress",progress);

        return  map;

    }

}
