package com.educonnect.service.strategy.impl;

import com.educonnect.model.user.Admin;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.User;
import com.educonnect.repo.AdminRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminAuthStrategyTest {

    @Mock private AdminRepo adminRepo;

    @InjectMocks
    private AdminAuthStrategy strategy;

    @Nested
    @DisplayName("supports(role)")
    class Supports {
        @Test
        void returns_true_for_ADMIN_case_insensitive() {
            assertThat(strategy.supports("ADMIN")).isTrue();
            assertThat(strategy.supports("admin")).isTrue();
            assertThat(strategy.supports("AdMiN")).isTrue();
        }

        @Test
        void returns_false_for_other_roles() {
            assertThat(strategy.supports("PARENT")).isFalse();
            assertThat(strategy.supports("STUDENT")).isFalse();
            assertThat(strategy.supports("TEACHER")).isFalse();
            assertThat(strategy.supports(null)).isFalse();
            assertThat(strategy.supports("")).isFalse();
        }
    }

    @Nested
    @DisplayName("save(User)")
    class Save {

        @Test
        void maps_fields_sets_ADMIN_role_and_calls_repo_save() {
            // Arrange
            User u = mock(User.class);
            when(u.getFullName()).thenReturn("Alice Admin");
            when(u.getEmail()).thenReturn("admin@example.com");
            when(u.getPassword()).thenReturn("secret");

            Admin saved = Admin.builder()
                    .fullName("Alice Admin")
                    .email("admin@example.com")
                    .password("secret")
                    .role(Role.ADMIN)
                    .build();

            when(adminRepo.save(any(Admin.class))).thenReturn(saved);

            ArgumentCaptor<Admin> captor = ArgumentCaptor.forClass(Admin.class);

            // Act
            User result = strategy.save(u);

            // Assert
            verify(adminRepo).save(captor.capture());
            Admin passed = captor.getValue();
            assertThat(passed.getFullName()).isEqualTo("Alice Admin");
            assertThat(passed.getEmail()).isEqualTo("admin@example.com");
            assertThat(passed.getPassword()).isEqualTo("secret");
            assertThat(passed.getRole()).isEqualTo(Role.ADMIN);

            assertThat(result).isInstanceOf(Admin.class);
            Admin returned = (Admin) result;
            assertThat(returned.getFullName()).isEqualTo("Alice Admin");
            assertThat(returned.getRole()).isEqualTo(Role.ADMIN);
        }
    }
}