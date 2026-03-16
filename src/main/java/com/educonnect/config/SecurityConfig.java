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
                                "/v1/api/assessment/quiz/**"

                ).permitAll()
                        .requestMatchers("/v1/api/result/**","/v1/api/teachers/**").hasRole("TEACHER")
                        .requestMatchers("/v1/api/parent/**").hasRole("PARENT")
                        .requestMatchers("/v1/api/assessment/create").hasRole("TEACHER")
                        .requestMatchers("/v1/api/student/**", "/v1/api/attendance/**", "/v1/api/doc/**", "/v1/api/assessment/submit")
                        .hasAnyRole("STUDENT","ADMIN")

                        .requestMatchers("/v1/api/audits/**").hasRole("ADMIN")

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
}
