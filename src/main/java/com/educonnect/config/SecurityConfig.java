package com.educonnect.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final EduconnectUserDetailsService educonnectUserDetailsService;
    private final JwtFilter jwtfilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception->
                        exception.accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v1/auth/**",
                                "/v1/api/course/**",
                                "/v1/api/attachment/view/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/index.html",
                                "/swagger-ui/**",
                                "/error"

                ).permitAll()
                        .requestMatchers("/v1/reports/student/**").hasAnyRole("STUDENT", "ADMIN")                        .requestMatchers("/v1/reports/course/**").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
                        .requestMatchers("/v1/reports/**").hasRole("ADMIN")
                        .requestMatchers("/v1/api/result/**","/v1/api/teachers/**").hasRole("TEACHER")
                        .requestMatchers("/v1/api/parent/**").hasRole("PARENT")
                        .requestMatchers("/v1/api/assessment/create").hasRole("TEACHER")
                        .requestMatchers(
                                "/v1/api/student/**",
                                "/v1/api/attendance/**",
                                "/v1/api/doc/**",
                                "/v1/api/assessment/report/**",
                                "/v1/api/assessment/get-assessment/**"
                        )
                        .hasAnyRole("STUDENT","ADMIN", "TEACHER")
                        .requestMatchers("/v1/api/assessment/submit").hasRole("STUDENT")

                        .requestMatchers("/v1/api/audits/**","/v1/api/compliance/**").hasRole("ADMIN")

                        .anyRequest().authenticated())
                        .sessionManagement(
                                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                        .formLogin(AbstractHttpConfigurer::disable)
                        .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(educonnectUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("authorization", "content-type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
