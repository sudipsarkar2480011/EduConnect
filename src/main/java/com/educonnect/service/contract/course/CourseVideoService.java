package com.educonnect.service.contract.course;

import com.educonnect.model.course.Course;
import com.educonnect.model.course.CourseModule;
import com.educonnect.model.user.Student;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service

/**
 * Service interface for handling video-based course content.
 * <p>Provides methods for managing video uploads, resource streaming,
 * deletions, and student progress tracking for specific modules.</p>
 *
 * @author sanchita das
 * @version 1.0
 * @since 1.0
 */

public interface CourseVideoService {
    /**
     * Processes and stores a video file as a course module.
     *
     * @param file the multipart video file to be uploaded.
     * @param title the display title of the module.
     * @param sequenceOrder the display order of this module within the course.
     * @param courseId the unique identifier of the parent course.
     * @return the created {@link CourseModule} entity.
     * @throws IOException if an error occurs during file storage.
     * @throws EncoderException if there is a failure in processing video metadata (like duration).
     */
    CourseModule uploadVideo(MultipartFile file, String title, Integer sequenceOrder, UUID courseId) throws IOException, EncoderException;
    /**
     * Generates a URI for accessing a video stream.
     *
     * @param id the unique identifier of the video module.
     * @return a formatted URL string for the video resource.
     * @throws IOException if the resource metadata cannot be accessed.
     */
    String getVideoUrl(UUID id) throws IOException;
    /**
     * Retrieves a video file as a system resource for streaming.
     *
     * @param id the unique identifier of the module.
     * @return a {@link Resource} representing the physical video file.
     * @throws IOException if the file is missing or inaccessible on disk.
     */
    Resource LoadVideoAsResource(UUID id) throws IOException;
    /**
     * Deletes a video resource using specific entity identifiers.
     *
     * @param videoId the ID of the video module to remove.
     * @param courseId the ID of the parent course for duration adjustment.
     * @return a status message confirming the deletion.
     * @throws IOException if the physical file deletion fails.
     */
    String deleteVideoResourceWithids(UUID videoId, UUID courseId) throws IOException;
    /**
     * Deletes a video resource directly using entity objects.
     *
     * @param video the {@link CourseModule} entity to be removed.
     * @param course the {@link Course} entity to be updated.
     * @return a status message confirming the deletion.
     * @throws IOException if the physical file deletion fails.
     */
    public String deleteVideoResource(CourseModule video, Course course) throws IOException;
    /**
     * Replaces an existing video file and updates module metadata.
     *
     * @param file the new multipart video file.
     * @param title the updated title of the module.
     * @param videoId the ID of the existing module to update.
     * @param courseId the ID of the parent course.
     * @return the updated {@link CourseModule} entity.
     * @throws IOException if file replacement fails.
     * @throws EncoderException if the new video metadata cannot be processed.
     */
    CourseModule updateVideoResource(MultipartFile file,String title,UUID videoId, UUID courseId) throws IOException, EncoderException;
    /**
     * Marks a specific module as viewed by a student and updates course progress.
     *
     * @param moduleId the unique identifier of the completed module.
     * @param courseId the unique identifier of the course.
     * @param student the {@link Student} entity whose progress is being updated.
     * @return a map containing completion details, updated progress percentage, and remaining time.
     */
    public Map<String, Object> markModuleAsCompleted(UUID moduleId, UUID courseId, Student student);
}
