package com.educonnect.notifications;

import com.educonnect.dto.course.CourseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * WHAT IS THIS?
 * This is a "Messenger" object. In an Event-Driven system, when a teacher
 * creates a new course, we don't want to make the teacher wait while we
 * send 1,000 notifications.
 * *HOW IT WORKS:
 * 1. The CourseService creates a course and "publishes" this event.
 * 2. This object carries just the essential data (ID and Title) across the system.
 * 3. A "Listener" catches this object in the background and starts the
 * notification process.
 * *  BENEFITS:
 * - Decoupling: CourseService doesn't need to know about the NotificationService.
 * - Performance: Helps handle heavy tasks in a separate background thread.
 */

@Data
@AllArgsConstructor
@Builder
public class CourseCreatedEvent {
    private UUID courseId;
    private String courseTitle;



}
