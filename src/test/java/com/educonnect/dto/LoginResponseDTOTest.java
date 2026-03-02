package com.educonnect.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseDTOTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void builder_shouldBuildAllFields() {
        LoginResponseDTO dto = LoginResponseDTO.builder()
                .token("jwt-token-123")
                .email("user@example.com")
                .role("STUDENT")
                .name("User Name")
                .build();

        assertThat(dto.getToken()).isEqualTo("jwt-token-123");
        assertThat(dto.getEmail()).isEqualTo("user@example.com");
        assertThat(dto.getRole()).isEqualTo("STUDENT");
        assertThat(dto.getName()).isEqualTo("User Name");
    }

    @Test
    void noArgsConstructorAndSetters_shouldPopulateFields() {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setToken("tkn");
        dto.setEmail("a@b.com");
        dto.setRole("TEACHER");
        dto.setName("A B");

        assertThat(dto.getToken()).isEqualTo("tkn");
        assertThat(dto.getEmail()).isEqualTo("a@b.com");
        assertThat(dto.getRole()).isEqualTo("TEACHER");
        assertThat(dto.getName()).isEqualTo("A B");
    }

    @Test
    void equalsAndHashCode_shouldWorkForSameValues() {
        LoginResponseDTO a = LoginResponseDTO.builder()
                .token("t1").email("e@x.com").role("ADMIN").name("N1").build();

        LoginResponseDTO b = LoginResponseDTO.builder()
                .token("t1").email("e@x.com").role("ADMIN").name("N1").build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void jsonRoundTrip_shouldPreserveValues() throws Exception {
        LoginResponseDTO original = LoginResponseDTO.builder()
                .token("jwt-token-xyz")
                .email("user@example.com")
                .role("STUDENT")
                .name("User Name")
                .build();

        String json = mapper.writeValueAsString(original);
        LoginResponseDTO restored = mapper.readValue(json, LoginResponseDTO.class);

        assertThat(restored).isEqualTo(original);
        assertThat(restored.getToken()).isEqualTo("jwt-token-xyz");
        assertThat(restored.getEmail()).isEqualTo("user@example.com");
        assertThat(restored.getRole()).isEqualTo("STUDENT");
        assertThat(restored.getName()).isEqualTo("User Name");
    }
}