package com.educonnect.dto.student;

import com.educonnect.model.course.Enrollment;
import com.educonnect.model.user.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponse{
       private  UUID userId;
       private  String fullName;
       private  String email;
       private  Role role;
       private  boolean active;
       private LocalDate dateOfBirth;
       private String enrollmentNumber;
       private String parentEmail;
 }