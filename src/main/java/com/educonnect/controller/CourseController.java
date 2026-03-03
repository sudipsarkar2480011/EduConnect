package com.educonnect.controller;

import com.educonnect.model.course.Course;
import com.educonnect.model.course.CourseModule;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.service.contract.course.CourseVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("v1/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseVideoService courseVideoServiceClass;
    private final CourseService courseService;

    @PostMapping("/add-video")
    public ResponseEntity<CourseModule> addVideo(
            @RequestParam MultipartFile file,
            @RequestParam String title,
            @RequestParam Integer sequenceOrder,
            @RequestParam UUID courseId) throws IOException, EncoderException {
        return ResponseEntity.ok( courseVideoServiceClass.uploadVideo(file,title,sequenceOrder,courseId));
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

    @PostMapping("/add-course")
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.addCourse(course));
    }

    @GetMapping("/get-course/{courseId}")
    public Course getcourse( @PathVariable UUID courseId ) throws Exception {
        return courseService.getByIdCourse(courseId);
    }

    @GetMapping("test")
    public String test(){
        return "test";
    }


}
