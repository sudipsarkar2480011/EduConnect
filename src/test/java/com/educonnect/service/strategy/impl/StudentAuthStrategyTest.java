package com.educonnect.service.strategy.impl;

import com.educonnect.model.user.Role;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.User;
import com.educonnect.repo.StudentRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentAuthStrategyTest {

    @Mock private StudentRepo studentRepo;

    @InjectMocks
    private StudentAuthStrategy strategy;

    @Nested
    @DisplayName("supports(role)")
    class Supports {
        @Test
        void returns_true_for_STUDENT_case_insensitive() {
            assertThat(strategy.supports("STUDENT")).isTrue();
            assertThat(strategy.supports("student")).isTrue();
            assertThat(strategy.supports("StUdEnT")).isTrue();
        }

        @Test
        void returns_false_for_other_roles() {
            assertThat(strategy.supports("ADMIN")).isFalse();
            assertThat(strategy.supports("PARENT")).isFalse();
            assertThat(strategy.supports("TEACHER")).isFalse();
            assertThat(strategy.supports(null)).isFalse();
            assertThat(strategy.supports("")).isFalse();
        }
    }

    @Nested
    @DisplayName("save(User)")
    class Save {

        @Test
        void maps_fields_sets_STUDENT_role_and_calls_repo_save() {
            // Arrange
            User u = mock(User.class);
            when(u.getFullName()).thenReturn("Shruti Student");
            when(u.getEmail()).thenReturn("student@example.com");
            when(u.getPassword()).thenReturn("secret");

            Student saved = Student.builder()
                    .fullName("Shruti Student")
                    .email("student@example.com")
                    .password("secret")
                    .role(Role.STUDENT)
                    .build();

            when(studentRepo.save(any(Student.class))).thenReturn(saved);

            ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);

            // Act
            User result = strategy.save(u);

            // Assert
            verify(studentRepo).save(captor.capture());
            Student passed = captor.getValue();
            assertThat(passed.getFullName()).isEqualTo("Shruti Student");
            assertThat(passed.getEmail()).isEqualTo("student@example.com");
            assertThat(passed.getPassword()).isEqualTo("secret");
            assertThat(passed.getRole()).isEqualTo(Role.STUDENT);

            assertThat(result).isInstanceOf(Student.class);
            Student returned = (Student) result;
            assertThat(returned.getRole()).isEqualTo(Role.STUDENT);
        }
    }
}