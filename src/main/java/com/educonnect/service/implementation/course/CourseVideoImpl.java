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
import lombok.RequiredArgsConstructor;
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

/**
 * Implementation of {@link CourseVideoService} for handling video content within courses.
 * This service manages the physical storage of video files, metadata extraction (duration),
 * and synchronization of total course duration when modules are added, updated, or removed.
    @author Sudip Sarkar
    @version 1.0
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseVideoImpl implements CourseVideoService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.tempUpload-dir}")
    private String tempUploadDir;


    private final CourseModuleRepo courseModuleRepo;
    private final CourseRepo courseRepo;
    private final EnrollmentRepo enrollmentRepo;


    /**
     * Uploads a video file, extracts its duration, and associates it with a Course as a Module.
     * * <p>Process:
     * 1. Validates file type.
     * 2. Saves to a temporary directory to extract metadata.
     * 3. Moves to permanent storage.
     * 4. Updates the total duration of the parent Course entity.</p>
     *
     * @param file          The multipart video file.
     * @param title         The display title for the module.
     * @param sequenceOrder The position of the video in the course syllabus.
     * @param courseId      The ID of the course this video belongs to.
     * @return The persisted {@link CourseModule} entity.
     * @throws IOException      If file writing or directory creation fails.
     * @throws EncoderException If video duration extraction fails.
     */
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

    /**
     * Generates a public streaming URL for a specific video module.
     * @param id The {@link UUID} of the course module.
     * @return A fully qualified URL string pointing to the streaming endpoint.
     */
    @Override
    public String getVideoUrl(UUID id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/v1/api/course/stream/")
                .path(id.toString())
                .toUriString();
    }

    /**
     * Loads a video file from the physical storage as a Spring {@link Resource}.
     * @param moduleId The ID of the module to retrieve.
     * @return The file resource for streaming or download.
     * @throws IOException If the file does not exist or is inaccessible.
     */
    @Override
    public Resource LoadVideoAsResource(UUID moduleId) throws IOException {
        CourseModule video = courseModuleRepo.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Video record not found"));

        Path path = Paths.get(uploadDir).resolve(video.getContentUrl()).normalize();

        if (!Files.exists(path)) throw new RuntimeException("File not found on disk");

        return new UrlResource(path.toUri());
    }

    /**
     * Deletes a video resource from both the database and the physical disk.
     * * @param videoId  The ID of the module/video to delete.
     * @param courseId The ID of the parent course.
     * @return A success message.
     * @throws IOException If physical file deletion fails.
     */
    @Override
    public String deleteVideoResourceWithids(UUID videoId, UUID courseId) throws IOException {
        CourseModule video = courseModuleRepo.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video record not found"));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));


        return deleteVideoResource(video,course);
    }

    /**
     * Internal logic for deleting a video resource.
     * Subtracts the video's duration from the total course duration before deletion.
     *
     * @param video  The {@link CourseModule} entity to remove.
     * @param course The {@link Course} entity to update.
     * @return Success confirmation message.
     * @throws IOException If disk cleanup fails.
     */
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

    /**
     * Replaces an existing video file with a new upload.
     * Updates the course's total duration by subtracting the old video length
     * and adding the new one.
     *
     * @param file     The new multipart video file.
     * @param title    The updated (or existing) title.
     * @param videoId  The ID of the existing module.
     * @param courseId The ID of the parent course.
     * @return The updated {@link CourseModule}.
     * @throws IOException      If file handling fails.
     * @throws EncoderException If duration extraction for the new file fails.
     */
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


    /**
     * Marks a specific module as completed for a student and updates their enrollment progress.
     *  <p>The progress is calculated as:
     * {@code (Total Course Duration - Remaining Duration) / Total Course Duration}</p>
     * @param moduleId The ID of the module completed.
     * @param courseId The ID of the course.
     * @param student  The {@link Student} performing the action.
     * @return A map containing the success message, new remaining time, and progress percentage.
     * @throws ResourceNotFoundException If enrollment or module records are missing.
     */
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
