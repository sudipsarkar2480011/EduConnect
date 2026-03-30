package com.educonnect.controller;

import com.educonnect.config.EduconnectUserDetailsService;
import com.educonnect.config.JWTService;
import com.educonnect.config.UserPrinciples;
import com.educonnect.model.course.CourseModule;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.service.contract.course.CourseVideoInerface;
import com.educonnect.service.contract.course.CourseVideoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(CourseControllerTest.TestMvcConfig.class)
public class CourseControllerTest {

    /**
     * FINAL APPROACH — set SecurityContextHolder directly per test.
     *
     * All previous approaches (.with(authentication(...)), .with(securityContext(...)),
     * @TestConfiguration + @Import) correctly put the principal into the session or
     * context, but @AuthenticationPrincipalArgumentResolver was either not registered
     * or not reading from the right place.
     *
     * Root issue: AuthenticationPrincipalArgumentResolver calls
     * SecurityContextHolder.getContext().getAuthentication() at resolution time.
     * With addFilters=false, no filter populates SecurityContextHolder from the request
     * session — it stays empty, so the resolver always gets null authentication and
     * injects null into the parameter.
     *
     * The fix: bypass all of this by setting SecurityContextHolder directly before
     * each test that needs a principal. The resolver reads from SecurityContextHolder
     * at argument resolution time, so setting it here (in the same thread as the test)
     * works reliably. Clean up in @AfterEach to prevent cross-test contamination.
     *
     * The @TestConfiguration + @Import still registers the resolver so Spring MVC
     * knows to invoke it at all. Both pieces are needed.
     */
    @TestConfiguration
    static class TestMvcConfig implements WebMvcConfigurer {
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new AuthenticationPrincipalArgumentResolver());
        }
    }

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

    @MockitoBean
    private CourseVideoInerface courseVideoInerface;

    private static final String BASE_URL = "/v1/api/course";

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    /**
     * Sets a Teacher principal directly into SecurityContextHolder.
     * AuthenticationPrincipalArgumentResolver reads from SecurityContextHolder
     * at invocation time (same thread), so this is always visible to it.
     */
    private void setTeacherPrincipal(UUID userId) {
        Teacher teacher = Teacher.builder()
                .userId(userId)
                .email("teacher@test.com")
                .password("password")
                .role(Role.TEACHER)
                .build();
        UserPrinciples up = new UserPrinciples(teacher);
        var auth = new UsernamePasswordAuthenticationToken(up, null, up.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void setStudentPrincipal(UUID userId) {
        Student student = Student.builder()
                .userId(userId)
                .email("student@test.com")
                .password("password")
                .role(Role.STUDENT)
                .build();
        UserPrinciples up = new UserPrinciples(student);
        var auth = new UsernamePasswordAuthenticationToken(up, null, up.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // ─── Tests ────────────────────────────────────────────────────────────────

    @Test
    void shouldUploadModuleSuccessfully() throws Exception {
        UUID courseId  = UUID.randomUUID();
        UUID moduleId  = UUID.randomUUID();
        UUID teacherId = UUID.randomUUID();

        setTeacherPrincipal(teacherId);

        MockMultipartFile file = new MockMultipartFile(
                "file", "intro.mp4", "video/mp4",
                "dummy-video-content".getBytes()
        );

        CourseModule module = CourseModule.builder()
                .moduleId(moduleId)
                .title("Intro")
                .sequenceOrder(1)
                .contentUrl("some-file-name.mp4")
                .duration(120.0)
                .build();

        when(courseVideoInerface.uploadVideo(any(), eq("Intro"), eq(courseId), eq(teacherId)))
                .thenReturn(module);

        mockMvc.perform(
                        multipart(BASE_URL + "/add-module")
                                .file(file)
                                .param("title",    "Intro")
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
        UUID videoId  = UUID.randomUUID();
        UUID userId   = UUID.randomUUID();

        setTeacherPrincipal(userId);

        MockMultipartFile file = new MockMultipartFile(
                "file", "updated.mp4", "video/mp4",
                "updated-video-content".getBytes()
        );

        CourseModule updatedModule = CourseModule.builder()
                .moduleId(videoId)
                .title("Updated Title")
                .sequenceOrder(2)
                .contentUrl("updated-file.mp4")
                .duration(240.0)
                .build();

        when(courseVideoService.updateVideoResource(any(), eq("Updated Title"), eq(videoId), eq(courseId), eq(userId)))
                .thenReturn(updatedModule);

        mockMvc.perform(
                        multipart(BASE_URL + "/" + courseId + "/video/" + videoId + "/update-video")
                                .file(file)
                                .param("title", "Updated Title")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.moduleId").value(videoId.toString()))
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void shouldDeleteVideoSuccessfully() throws Exception {
        UUID courseId = UUID.randomUUID();
        UUID videoId  = UUID.randomUUID();
        UUID userId   = UUID.randomUUID();

        setTeacherPrincipal(userId);

        String successMsg =
                "Successfully deleted the video with title Intro of course with title Java Masterclass";

        when(courseVideoService.deleteVideoResourceWithids(eq(videoId), eq(courseId), eq(userId)))
                .thenReturn(successMsg);

        mockMvc.perform(
                        delete(BASE_URL + "/" + courseId + "/video/" + videoId + "/delete-video")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(successMsg));
    }

    @Test
    void shouldGetModuleUrl() throws Exception {
        UUID videoId = UUID.randomUUID();
        String mockUrl = "http://localhost/v1/api/course/stream/" + videoId;

        when(courseVideoService.getVideoUrl(videoId)).thenReturn(mockUrl);

        mockMvc.perform(get(BASE_URL + "/get-module/" + videoId))
                .andExpect(status().isOk())
                .andExpect(content().string(mockUrl));
    }

    @Test
    void shouldStreamVideo() throws Exception {
        UUID moduleId = UUID.randomUUID();
        byte[] bytes  = new byte[]{1, 2, 3, 4, 5};

        Resource resource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() { return "test.mp4"; }
        };

        when(courseVideoService.LoadVideoAsResource(moduleId)).thenReturn(resource);

        mockMvc.perform(get(BASE_URL + "/stream/" + moduleId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "video/mp4"))
                .andExpect(content().bytes(bytes));
    }

    @Test
    void shouldMarkModuleAsCompleted() throws Exception {
        UUID courseId  = UUID.randomUUID();
        UUID moduleId  = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        setStudentPrincipal(studentId);

        Map<String, String> result = Map.of("status", "completed");

        when(courseVideoService.markModuleAsCompleted(eq(moduleId), eq(courseId), any(Student.class)))
                .thenReturn(result);

        mockMvc.perform(
                        post(BASE_URL + "/" + courseId + "/module/" + moduleId + "/mark-as-complete")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("completed"))
                .andExpect(jsonPath("$.message").value("Module with id " + moduleId + " marked as done"))
                .andExpect(jsonPath("$.httpStatus").value(200));
    }

    @Test
    void shouldReturnTestString() throws Exception {
        mockMvc.perform(get(BASE_URL + "/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("test"));
    }
}