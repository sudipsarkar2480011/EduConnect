package com.educonnect.notifications;

import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.notification.NotificationType;
import com.educonnect.service.contract.StudentService;
import com.educonnect.service.contract.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * WHAT IS THIS?
 * This is the "Ear" of the notification system. It sits in the background
 * and waits for specific things to happen in the application.
 * * KEY FEATURES:
 * 1. Event Listening: It uses @EventListener to automatically trigger whenever
 * a 'CourseCreatedEvent' is published by the CourseService.
 * 2. Non-Blocking: Because of @Async, this logic runs on a separate "Worker Thread."
 * The teacher who created the course doesn't have to wait for this to finish.
 * 3. Error Handling: It uses a try-catch block to ensure that if something
 * goes wrong with the notifications, it doesn't crash the main course creation.
 * This class implements the "Observer Pattern." It allows the Course module
 * to talk to the Notification module without them being tightly coupled.
 */

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final StudentService studentService;

    private final NotificationService notificationService;

    /**
     * This method catches the 'CourseCreatedEvent' and tells the
     * NotificationService to generate records for every student.
     * * @Async - Runs this in a background thread.
     * @EventListener - Tells Spring to call this method when the event is fired.
     */

    @Async
    @EventListener
    public void handleCourseCreated(CourseCreatedEvent event) {
        try {
            notificationService.createNotificationsForAllStudents(
                    event.getCourseId(),
                    event.getCourseTitle(),
                    NotificationType.COURSE_CREATION
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
