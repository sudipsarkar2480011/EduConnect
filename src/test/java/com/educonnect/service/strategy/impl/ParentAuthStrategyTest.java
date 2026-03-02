package com.educonnect.service.strategy.impl;

import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.User;
import com.educonnect.repo.ParentRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentAuthStrategyTest {

    @Mock private ParentRepo parentRepo;

    @InjectMocks
    private ParentAuthStrategy strategy;

    @Nested
    @DisplayName("supports(role)")
    class Supports {
        @Test
        void returns_true_for_PARENT_case_insensitive() {
            assertThat(strategy.supports("PARENT")).isTrue();
            assertThat(strategy.supports("parent")).isTrue();
            assertThat(strategy.supports("PaReNt")).isTrue();
        }

        @Test
        void returns_false_for_other_roles() {
            assertThat(strategy.supports("ADMIN")).isFalse();
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
        void maps_fields_sets_PARENT_role_and_calls_repo_save() {
            // Arrange
            User u = mock(User.class);
            when(u.getFullName()).thenReturn("Prakash Parent");
            when(u.getEmail()).thenReturn("parent@example.com");
            when(u.getPassword()).thenReturn("secret");

            Parent saved = Parent.builder()
                    .fullName("Prakash Parent")
                    .email("parent@example.com")
                    .password("secret")
                    .role(Role.PARENT)
                    .build();

            when(parentRepo.save(any(Parent.class))).thenReturn(saved);

            ArgumentCaptor<Parent> captor = ArgumentCaptor.forClass(Parent.class);

            // Act
            User result = strategy.save(u);

            // Assert
            verify(parentRepo).save(captor.capture());
            Parent passed = captor.getValue();
            assertThat(passed.getFullName()).isEqualTo("Prakash Parent");
            assertThat(passed.getEmail()).isEqualTo("parent@example.com");
            assertThat(passed.getPassword()).isEqualTo("secret");
            assertThat(passed.getRole()).isEqualTo(Role.PARENT);

            assertThat(result).isInstanceOf(Parent.class);
            Parent returned = (Parent) result;
            assertThat(returned.getRole()).isEqualTo(Role.PARENT);
        }
    }
}