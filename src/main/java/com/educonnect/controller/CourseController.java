package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.dto.course.CourseRequestDTO;
import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.course.ModuleRequestDTO;
import com.educonnect.dto.course.ModuleResponseDTO;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.course.CourseModule;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.service.contract.course.CourseVideoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/course")
@RequiredArgsConstructor
@Tag(name = "08 CourseController")

/**
 * REST controller for handling course related requests
 * Delegates business logic for course related operations
 */
public class CourseController {
    private final CourseVideoService courseVideoServiceClass;
    private final CourseService courseService;

    @PostMapping("/enrollment/{courseId}/student/{studentId}")
    public ResponseEntity<StudentResponse> enrollStudent(
            @PathVariable("courseId") UUID courseId,
            @PathVariable("studentId") UUID studentId
    ) throws UserNotFoundException {
        return new ResponseEntity<>(
                courseService.addStudentToCourse(studentId,courseId),
                HttpStatus.OK
        );
    }

    /**
     * handles adding the course
     * @param courseRequest The payload
     * @param userPrinciple AuthenticationPrincipal
     * @return ResponseEntity of {@link CourseResponseDTO}
     */
    @PostMapping("/add-course")
    public ResponseEntity<CourseResponseDTO> addCourse(
            @RequestBody CourseRequestDTO courseRequest,
            @AuthenticationPrincipal UserPrinciples userPrinciple
    ) {
        return ResponseEntity.ok(
                courseService.addCourse(courseRequest, (Teacher) userPrinciple.getUser())
        );
    }

    @PostMapping("/add-video")
    public ResponseEntity<CourseModule> addVideo(
            @RequestParam MultipartFile file,
            @RequestParam String title,
            @RequestParam Integer sequenceOrder,
            @RequestParam UUID courseId) throws IOException, EncoderException {
        return ResponseEntity.ok(courseVideoServiceClass.uploadVideo(file,title,sequenceOrder,courseId));
    }

    /**
     * handles video updation request
     * @param file The new file
     * @param courseId The unique identifier of the course whose {@link CourseModule}(video) is to be updated
     * @param title Title of the video
     * @param videoId The unique identifier of the video(module) that is to be updated(changed with the new video)
     * @return ResponseEntity of {@link CourseModule}
     * @throws IOException
     * @throws EncoderException
     */
    @PostMapping("/{courseId}/video/{videoId}/update-video")
    public ResponseEntity<CourseModule> updateVideo(
            @RequestParam MultipartFile file,
            @PathVariable UUID courseId,
            @RequestParam String title,
            @PathVariable UUID videoId) throws IOException, EncoderException {

        return ResponseEntity.ok(
                courseVideoServiceClass.updateVideoResource(file,title,videoId,courseId)
        );
    }

    /**
     * handles video deletion
     * @param courseId The unique identifier of the course whose {@link CourseModule}(video) is to be deleted
     * @param videoId The unique identifier of the video(module) that is to be deleted
     * @return ResponseEntity of {@link CourseModule}
     * @return Success message
     * @throws IOException
     * @throws EncoderException
     */

    @DeleteMapping("/{courseId}/video/{videoId}/delete-video")
    public ResponseEntity<String> deleteVideo(
            @PathVariable UUID courseId,
            @PathVariable UUID videoId) throws IOException, EncoderException {

        return ResponseEntity.ok(
                courseVideoServiceClass.deleteVideoResourceWithids(videoId,courseId)
        );
    }


    @GetMapping("/get-video/{id}")
    public ResponseEntity<String> getVideo(@PathVariable UUID id) throws IOException {
        String url=courseVideoServiceClass.getVideoUrl(id);
        return ResponseEntity.ok(url);
    }


    @GetMapping("/stream/{filename}")
    public ResponseEntity<Resource> streamVideo(@PathVariable UUID filename) throws IOException
    {
        Resource resource= courseVideoServiceClass.LoadVideoAsResource(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(resource);
    }


    @GetMapping("/get-course/{courseId}")
    public CourseResponseDTO getcourse( @PathVariable UUID courseId ) throws Exception {
        return courseService.getByIdCourse(courseId);
    }



    @GetMapping("all-course")
    public ResponseEntity<List<ModuleResponseDTO>> getAllModulesOfaCourse(@RequestBody ModuleRequestDTO requestDTO)
    {
        return new ResponseEntity<>(courseService.getAllModulesOfACourse(requestDTO.courseId()),HttpStatus.OK);
    }

    @PostMapping("/{courseId}/module/{moduleId}/mark-as-complete")
    public ResponseEntity<Object> markModuleAsCompleted(
            @PathVariable("moduleId") UUID moduleId,
            @PathVariable("courseId") UUID courseId,
            @AuthenticationPrincipal UserPrinciples userPrinciple
    ){
        return new ResponseEntity<>(
                courseVideoServiceClass.markModuleAsCompleted(
                        moduleId,
                        courseId,
                        (Student) userPrinciple.getUser()),
                HttpStatus.OK
        );
    }

    @GetMapping("test")
    public String test(){
        return "test";
    }


}
