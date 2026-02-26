package com.educonnect.controller;

import com.educonnect.model.course.Course;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.service.contract.course.CourseVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseVideoService courseVideoServiceClass;

    @Autowired
    private CourseService courseService;

    @PostMapping("/add-video")
    public ResponseEntity<String> addVideo(
            @RequestParam MultipartFile file,
            @RequestParam String title,
            @RequestParam Integer sequenceOrder,
            @RequestParam UUID courseId) throws IOException {
        courseVideoServiceClass.uploadVideo(file,title,sequenceOrder,courseId);
        return ResponseEntity.ok("Video uploaded successfully");
    }
    @GetMapping("/get-video/{videoId}")
    public ResponseEntity<Resource> getVideo(@PathVariable UUID videoId) throws IOException {
        Resource resource = courseVideoServiceClass.getVideo(videoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4") // Or detect dynamically
                .body(resource);
    }

    @PostMapping("/add-course")
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        // Validation logic here
        return ResponseEntity.ok(courseService.addCourse(course));
    }
    @GetMapping("/get-course/{courId}")
    public Course getcourse( @PathVariable UUID courseId ) throws Exception {
        return courseService.getByIdCourse(courseId);
    }


}
