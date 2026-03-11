package com.educonnect.controller;

import com.educonnect.config.EduconnectUserDetailsService;
import com.educonnect.config.JWTService;
import com.educonnect.model.course.CourseModule;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.service.contract.course.CourseVideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
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

    private static final String BASE_URL = "/v1/api/course";

    @Test
    void shouldUploadVideoSuccessfully() throws Exception {
        UUID courseId = UUID.randomUUID();
        UUID moduleId = UUID.randomUUID();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "intro.mp4",
                "video/mp4",
                "dummy-video-content".getBytes()
        );

        CourseModule module = CourseModule.builder()
                .moduleId(moduleId)
                .title("Intro")
                .sequenceOrder(1)
                .contentUrl("some-file-name.mp4")
                .duration(120.0)
                .build();

        when(courseVideoService.uploadVideo(any(), eq("Intro"), eq(1), eq(courseId)))
                .thenReturn(module);

        mockMvc.perform(
                        multipart(BASE_URL + "/add-video")
                                .file(file)
                                .param("title", "Intro")
                                .param("sequenceOrder", "1")
                                .param("courseId", courseId.toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.moduleId").value(moduleId.toString()))
                .andExpect(jsonPath("$.title").value("Intro"))
                .andExpect(jsonPath("$.sequenceOrder").value(1));
    }

    @Test
    void shouldUpdateVideoSuccessfully() throws Exception {
        UUID courseId = UUID.randomUUID();
        UUID videoId = UUID.randomUUID();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "updated.mp4",
                "video/mp4",
                "updated-video-content".getBytes()
        );

        CourseModule updatedModule = CourseModule.builder()
                .moduleId(videoId)
                .title("Updated Title")
                .sequenceOrder(2)
                .contentUrl("updated-file.mp4")
                .duration(240.0)
                .build();

        when(courseVideoService.updateVideoResource(any(), eq("Updated Title"), eq(videoId), eq(courseId)))
                .thenReturn(updatedModule);

        mockMvc.perform(
                        multipart(BASE_URL + "/" + courseId + "/video/" + videoId + "/update-video")
                                .file(file)
                                .param("title", "Updated Title")
                        // NOTE: controller only takes "title" as param, file is multipart
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.moduleId").value(videoId.toString()))
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void shouldDeleteVideoSuccessfully() throws Exception {
        UUID courseId = UUID.randomUUID();
        UUID videoId = UUID.randomUUID();

        String successMsg = "Successfully deleted the video with title Intro of course with title Java Masterclass";

        when(courseVideoService.deleteVideoResourceWithids(videoId, courseId))
                .thenReturn(successMsg);

        mockMvc.perform(
                        delete(BASE_URL + "/" + courseId + "/video/" + videoId + "/delete-video")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(successMsg));
    }

    @Test
    void shouldGetVideoUrl() throws Exception {
        UUID videoId = UUID.randomUUID();
        String mockUrl = "http://localhost/v1/api/course/stream/" + videoId;

        when(courseVideoService.getVideoUrl(videoId)).thenReturn(mockUrl);

        mockMvc.perform(get(BASE_URL + "/get-video/" + videoId))
                .andExpect(status().isOk())
                .andExpect(content().string(mockUrl));
    }

    @Test
    void shouldStreamVideo() throws Exception {
        UUID moduleId = UUID.randomUUID();
        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        Resource resource = new ByteArrayResource(bytes);

        when(courseVideoService.LoadVideoAsResource(moduleId)).thenReturn(resource);

        mockMvc.perform(get(BASE_URL + "/stream/" + moduleId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "video/mp4"))
                .andExpect(content().bytes(bytes));
    }

    @Test
    void shouldReturnUpdateUrlForIdUpdateEndpoint() throws Exception {
        UUID moduleId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        // This endpoint returns a URL using getVideoUrl(id) and ignores other params for now
        String mockUrl = "http://localhost/v1/api/course/stream/" + moduleId;

        when(courseVideoService.getVideoUrl(moduleId)).thenReturn(mockUrl);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "any.mp4",
                "video/mp4",
                "bytes".getBytes()
        );

        mockMvc.perform(
                        multipart(BASE_URL + "/" + moduleId + "/update")
                                .file(file)
                                .param("title", "Any")
                                .param("sequenceOrder", "1")
                                .param("courseId", courseId.toString())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(mockUrl));
    }
}