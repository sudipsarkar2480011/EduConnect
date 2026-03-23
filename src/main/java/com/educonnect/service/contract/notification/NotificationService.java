package com.educonnect.service.contract.notification;

import com.educonnect.dto.notification.NotificationResponseDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.notification.Notification;
import com.educonnect.model.notification.NotificationType;

import java.util.List;
import java.util.UUID;

/**
 * WHAT IS THIS?
 * This is the "Contract" for the Notification system. It defines the
 * rules for how other parts of the app (like CourseService or the Controller)
 * can interact with notifications.
 * * BUSINESS LOGIC DEFINED:
 * 1. Mass Alerting: The ability to notify all students at once when a new
 * course is added.
 * 2. User Interaction: Marking alerts as 'seen' so the student's UI stays clean.
 * 3. Data Retrieval: Fetching a user's notification history (as DTOs to avoid loops).
 * 4. Maintenance: Automatically cleaning up the database to save storage space.
 * "I used an interface here to follow the 'Dependency Inversion Principle' (the 'D' in SOLID).
 * This allows me to swap the implementation later—for example, if we decide
 * to send Emails or SMS instead of just saving to the database—without
 * changing the code that calls these methods."
 */

public interface NotificationService {
    /**
     * 📢 BATCH CREATION
     * Triggered by the Listener. It finds all students and creates
     * notification records for them in bulk.
     */
    public void createNotificationsForAllStudents(UUID courseId, String title, NotificationType type);

    /**
     * ✅ UI UPDATE
     * Clears the 'unread' count for a student. Uses a bulk database
     * update for high performance.
     */
    public void markAllAsSeen(UUID userId);

    /**
     * 📄 DATA FETCHING
     * Returns a list of notifications for the frontend.
     * Note: Returns 'NotificationResponseDTO' instead of the Entity
     * to prevent Circular Dependency/Infinite Recursion during JSON conversion.
     */

   public List<NotificationResponseDTO> getNotificationsByUserId(UUID userId);

    /**
     * ✅ UI UPDATE
     * Clears the 'unread' count for a student. Uses a bulk database
     * update for high performance.
     */
    public void cleanOldNotifications();

}
