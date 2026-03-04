package com.educonnect.controller;

import com.educonnect.config.EduconnectUserDetailsService;
import com.educonnect.config.JWTService;
import com.educonnect.model.course.Course;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.service.contract.course.CourseVideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EduconnectUserDetailsService educonnectUserDetailsService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private CourseVideoService courseVideoService;

    @Test
    void shouldAddCourseSuccessfully() throws Exception {
        // Arrange
        Course course = new Course();
        course.setTitle("Java Masterclass");

        when(courseService.addCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/course/add-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseName\": \"Java Masterclass\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Masterclass"));
    }

    @Test
    void shouldGetVideoUrl() throws Exception {
        UUID videoId = UUID.randomUUID();
        String mockUrl = "http://storage.educonnect.com/video123.mp4";

        when(courseVideoService.getVideoUrl(videoId)).thenReturn(mockUrl);

        mockMvc.perform(get("/course/get-video/" + videoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(mockUrl));
    }
}