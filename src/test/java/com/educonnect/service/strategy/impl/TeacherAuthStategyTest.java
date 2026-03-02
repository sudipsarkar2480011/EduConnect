package com.educonnect.service.strategy.impl;

import com.educonnect.model.user.Role;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import com.educonnect.repo.TeacherRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherAuthStategyTest {

    @Mock private TeacherRepo teacherRepo;

    @InjectMocks
    private TeacherAuthStategy strategy;

    @Nested
    @DisplayName("supports(role)")
    class Supports {
        @Test
        void returns_true_for_TEACHER_case_insensitive() {
            assertThat(strategy.supports("TEACHER")).isTrue();
            assertThat(strategy.supports("teacher")).isTrue();
            assertThat(strategy.supports("TeAcHeR")).isTrue();
        }

        @Test
        void returns_false_for_other_roles() {
            assertThat(strategy.supports("ADMIN")).isFalse();
            assertThat(strategy.supports("PARENT")).isFalse();
            assertThat(strategy.supports("STUDENT")).isFalse();
            assertThat(strategy.supports(null)).isFalse();
            assertThat(strategy.supports("")).isFalse();
        }
    }

    @Nested
    @DisplayName("save(User)")
    class Save {

        @Test
        void maps_fields_sets_TEACHER_role_and_calls_repo_save() {
            // Arrange
            User u = mock(User.class);
            when(u.getFullName()).thenReturn("Tarun Teacher");
            when(u.getEmail()).thenReturn("teacher@example.com");
            when(u.getPassword()).thenReturn("secret");

            Teacher saved = Teacher.builder()
                    .fullName("Tarun Teacher")
                    .email("teacher@example.com")
                    .password("secret")
                    .role(Role.TEACHER)
                    .build();

            when(teacherRepo.save(any(Teacher.class))).thenReturn(saved);

            ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);

            // Act
            User result = strategy.save(u);

            // Assert
            verify(teacherRepo).save(captor.capture());
            Teacher passed = captor.getValue();
            assertThat(passed.getFullName()).isEqualTo("Tarun Teacher");
            assertThat(passed.getEmail()).isEqualTo("teacher@example.com");
            assertThat(passed.getPassword()).isEqualTo("secret");
            assertThat(passed.getRole()).isEqualTo(Role.TEACHER);

            assertThat(result).isInstanceOf(Teacher.class);
            Teacher returned = (Teacher) result;
            assertThat(returned.getRole()).isEqualTo(Role.TEACHER);
        }
    }
}
