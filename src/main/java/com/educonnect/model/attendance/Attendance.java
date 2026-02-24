package com.educonnect.model.attendance;


//AttendanceID, StudentID, CourseID, Date, Status

import com.educonnect.model.course.Course;
import com.educonnect.model.user.Student;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID AttendanceID;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;


}
