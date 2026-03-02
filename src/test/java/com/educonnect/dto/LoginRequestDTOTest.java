package com.educonnect.dto;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginRequestDTOTest {

    // OPTIONAL: If you want to assert that a default constructor truly does NOT exist, keep this test.
    // If you don't need it, you can delete this method.
    @Test
    void defaultConstructor_shouldNotExist() {
        assertThrows(NoSuchMethodException.class, () -> {
            LoginRequestDTO.class.getDeclaredConstructor();
        });
    }

    @Test
    void shouldCreateUsingAllArgsConstructor_andReadValuesViaReflection() throws Exception {
        // Instantiate using the available 3-arg constructor
        LoginRequestDTO req = new LoginRequestDTO("user@example.com", "Secret123", "STUDENT");

        // Read fields via reflection (in case there are no getters)
        Field email = LoginRequestDTO.class.getDeclaredField("email");
        Field password = LoginRequestDTO.class.getDeclaredField("password");
        Field role = LoginRequestDTO.class.getDeclaredField("role");

        email.setAccessible(true);
        password.setAccessible(true);
        role.setAccessible(true);

        assertThat(email.get(req)).isEqualTo("user@example.com");
        assertThat(password.get(req)).isEqualTo("Secret123");
        assertThat(role.get(req)).isEqualTo("STUDENT");
    }

    @Test
    void reflection_shouldSetAndGetPrivateFields() throws Exception {
        // Start with a valid instance
        LoginRequestDTO req = new LoginRequestDTO("user@example.com", "Secret123", "STUDENT");

        Field email = LoginRequestDTO.class.getDeclaredField("email");
        Field password = LoginRequestDTO.class.getDeclaredField("password");
        Field role = LoginRequestDTO.class.getDeclaredField("role");

        email.setAccessible(true);
        password.setAccessible(true);
        role.setAccessible(true);

        // Mutate values via reflection
        email.set(req, "user@example.com");
        password.set(req, "Secret123!");
        role.set(req, "STUDENT");

        // Assert mutated state
        assertThat(email.get(req)).isEqualTo("user@example.com");
        assertThat(password.get(req)).isEqualTo("Secret123!");
        assertThat(role.get(req)).isEqualTo("STUDENT");
    }

    @Test
    void fields_shouldExistWithExpectedNamesAndTypes() throws Exception {
        Field email = LoginRequestDTO.class.getDeclaredField("email");
        Field password = LoginRequestDTO.class.getDeclaredField("password");
        Field role = LoginRequestDTO.class.getDeclaredField("role");

        assertThat(email.getType()).isEqualTo(String.class);
        assertThat(password.getType()).isEqualTo(String.class);
        assertThat(role.getType()).isEqualTo(String.class);
    }
}