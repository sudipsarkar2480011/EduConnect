package com.educonnect.service.implementation.course;

import com.educonnect.model.course.Course;
import com.educonnect.model.user.Teacher;
import com.educonnect.repo.TeacherRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.contract.course.CourseVideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private TeacherRepo teacherRepo;

    // Not used in current implementation, but exists in the service
    @Mock
    private CourseVideoService courseVideoService;

    @InjectMocks
    private CourseServiceImpl courseService; // Mocks injected by reflection

    private UUID teacherId;
    private Teacher teacher;
    private Course course;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        teacherId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        teacher = new Teacher();
        // assuming Teacher has setUserId(UUID)
        teacher.setUserId(teacherId);

        course = new Course();
        // assuming Course has setId(UUID), setTeacher(Teacher)
        // (adapt setters/getters to your actual model)
        try {
            course.getClass().getMethod("setId", UUID.class).invoke(course, courseId);
        } catch (Exception ignored) {
            // If Course doesn't have setId, tests that rely on ID won't use it.
        }
        course.setTeacher(teacher);
    }

    @Test
    void addCourse_ShouldAttachManagedTeacher_AndSave() {
        // Arrange
        when(teacherRepo.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(courseRepo.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Course saved = courseService.addCourse(course);

        // Assert
        assertNotNull(saved);
        assertSame(teacher, saved.getTeacher(), "Service should replace teacher with managed Teacher from repo");
        verify(teacherRepo, times(1)).findById(teacherId);
        verify(courseRepo, times(1)).save(course);
        verifyNoMoreInteractions(courseRepo, teacherRepo);
    }

    @Test
    void addCourse_ShouldThrow_WhenTeacherNotFound() {
        // Arrange
        when(teacherRepo.findById(teacherId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> courseService.addCourse(course));
        assertTrue(ex.getMessage().contains("Teacher not found"), "Exception message should mention teacher not found");

        verify(teacherRepo, times(1)).findById(teacherId);
        verify(courseRepo, never()).save(any());
    }

    @Test
    void getAllCourse_ShouldReturnAllFromRepo() {
        // Arrange
        List<Course> list = Arrays.asList(new Course(), new Course());
        when(courseRepo.findAll()).thenReturn(list);

        // Act
        List<Course> result = courseService.getAllCourse();

        // Assert
        assertEquals(2, result.size());
        assertIterableEquals(list, result);
        verify(courseRepo, times(1)).findAll();
        verifyNoMoreInteractions(courseRepo);
    }

    @Test
    void getByIdCourse_ShouldReturnCourse_WhenPresent() throws Exception {
        // Arrange
        when(courseRepo.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        Course found = courseService.getByIdCourse(courseId);

        // Assert
        assertNotNull(found);
        assertSame(course, found);
        verify(courseRepo, times(1)).findById(courseId);
        verifyNoMoreInteractions(courseRepo);
    }

    @Test
    void getByIdCourse_ShouldThrowException_WhenNotPresent() {
        // Arrange
        when(courseRepo.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception ex = assertThrows(Exception.class, () -> courseService.getByIdCourse(courseId));
        assertTrue(ex.getMessage().contains("no course present"), "Should propagate custom not-found message");
        verify(courseRepo, times(1)).findById(courseId);
        verifyNoMoreInteractions(courseRepo);
    }

    @Test
    void deleteById_ShouldDeleteAndReturnMessage() {
        // Arrange
        doNothing().when(courseRepo).deleteById(courseId);

        // Act
        String msg = courseService.deleteById(courseId);

        // Assert
        assertEquals("Deleted", msg);
        verify(courseRepo, times(1)).deleteById(courseId);
        verifyNoMoreInteractions(courseRepo);
    }
}