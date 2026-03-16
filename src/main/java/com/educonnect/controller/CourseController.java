package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.dto.common.GenericResponse;
import com.educonnect.dto.course.CourseRequestDTO;
import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.course.ModuleRequestDTO;
import com.educonnect.dto.course.ModuleResponseDTO;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.exception.custom_exceptions.UserIdDoNothMatchException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.course.CourseModule;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.service.contract.course.CourseVideoService;
import com.educonnect.service.contract.course.CourseVideoInerface;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    private final CourseVideoInerface courseVideoInerface;

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

    @PostMapping("/add-module")
    public ResponseEntity<CourseModule> addVideo(
            @RequestParam MultipartFile file,
            @RequestParam String title,
            @RequestParam UUID courseId,
            @AuthenticationPrincipal UserPrinciples principles
    ) throws IOException, EncoderException, UserIdDoNothMatchException {
        UUID teacherId=principles.getUser().getUserId();
      return ResponseEntity.ok(courseVideoInerface.uploadVideo(file,title,courseId ,teacherId));
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
            @PathVariable UUID videoId,
            @AuthenticationPrincipal UserPrinciples userPrinciples) throws IOException, EncoderException {
        UUID userId= userPrinciples.getUser().getUserId();
        return ResponseEntity.ok(
                courseVideoServiceClass.updateVideoResource(file,title,videoId,courseId,userId)
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
            @PathVariable UUID videoId,
            @AuthenticationPrincipal UserPrinciples userprincipal
    ) throws IOException, EncoderException, UserIdDoNothMatchException {

        UUID id=userprincipal.getUser().getUserId();
        return ResponseEntity.ok(
                courseVideoServiceClass.deleteVideoResourceWithids(videoId,courseId,id)
        );
    }


    @GetMapping("/get-video/{id}")
    public ResponseEntity<String> getVideo(@PathVariable UUID id) throws IOException {
        String url=courseVideoServiceClass.getVideoUrl(id);
        return ResponseEntity.ok(url);
    }


    @GetMapping("/stream/{moduleId}")
    public ResponseEntity<Resource> streamContent(@PathVariable UUID moduleId) throws IOException {
        Resource resource = courseVideoServiceClass.LoadVideoAsResource(moduleId);
        String filename = resource.getFilename();
        String contentType = "application/octet-stream";

        if (filename != null) {
            if (filename.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (filename.endsWith(".mp3")) {
                contentType = "audio/mpeg";
            } else if (filename.endsWith(".mp4")) {
                contentType = "video/mp4";
            }
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }


    @GetMapping("/get-course/{courseId}")
    public CourseResponseDTO getcourse( @PathVariable UUID courseId ) throws Exception {
        return courseService.getByIdCourse(courseId);
    }



    @GetMapping("modules")
    public ResponseEntity<List<ModuleResponseDTO>> getAllModulesOfaCourse(@RequestBody ModuleRequestDTO requestDTO)
    {
        return new ResponseEntity<>(courseService.getAllModulesOfACourse(requestDTO.courseId()),HttpStatus.OK);
    }

    @PostMapping("/{courseId}/module/{moduleId}/mark-as-complete")
    public ResponseEntity<GenericResponse<Map<String,Double>>> markModuleAsCompleted(
            @PathVariable("moduleId") UUID moduleId,
            @PathVariable("courseId") UUID courseId,
            @AuthenticationPrincipal UserPrinciples userPrinciple
    ){
        var resp = courseVideoServiceClass.markModuleAsCompleted(
                moduleId,
                courseId,
                (Student) userPrinciple.getUser());
        return new ResponseEntity<>(
                new GenericResponse<>(
                        resp,
                        "Module with id " + moduleId+" marked as done",
                        HttpStatus.OK.value()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("test")
    public String test(){
        return "test";
    }


}
