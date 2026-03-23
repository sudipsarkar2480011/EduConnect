package com.educonnect.service.implementation;

import com.educonnect.config.UserRepo;
import com.educonnect.dto.notification.NotificationResponseDTO;
import com.educonnect.model.notification.Notification;
import com.educonnect.model.notification.NotificationType;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.User;
import com.educonnect.repo.NotificationRepo;
import com.educonnect.service.contract.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * ⚙️ SERVICE IMPLEMENTATION: Notification Engine
 * * This class serves as the central hub for all notification logic. It connects
 * the User domain with the Notification domain and ensures high-performance
 * data operations.
 * 1. Constructor Injection: Uses Lombok's {@link RequiredArgsConstructor} for
 * better testability and immutability (final fields).
 * 2. SLF4J Logging: Implements professional logging to track background tasks
 * like the weekly database cleanup.
 * 3. Stream API: Employs functional programming to transform data efficiently
 * without verbose 'for' loops.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final NotificationRepo notificationRepo;

    private final UserRepo userRepo;

    /**
     *  MASS NOTIFICATION (Event-Driven)
     * * Triggered by a background event when a course is created.
     * - Instead of saving in a loop, we collect all students and use 'saveAll'.
     * - This triggers JDBC Batching (defined in application.properties) to
     * reduce database round-trips.
     * - @Transactional ensures that we don't end up with "partial" notifications
     * if the server crashes mid-process.
     */
    @Override
    @Transactional
    public void createNotificationsForAllStudents(UUID courseId, String title, NotificationType type) {
        List<User> students = userRepo.findAllByRole(Role.STUDENT);
        List<Notification> notifications = students.stream()
                .map(student -> Notification.builder()
                        .user(student)
                        .courseId(courseId)
                        .message("NEW COURSE CREATED: " + title)
                        .category(type)
                        .status(false)
                        .createdDate(LocalDateTime.now())
                        .build())
                .toList();

        notificationRepo.saveAll(notifications);
    }
    /**
     *  BULK READ STATUS UPDATE
     * * Uses a custom JPQL query in the repository to update all 'unread'
     * status flags to 'true' for a specific user.
     */

    @Override
    public void markAllAsSeen(UUID userId) {
        notificationRepo.markAllAsReadByUserId(userId);
    }

    /**
     * FETCHING DATA (Anti-Circular Dependency)
     * * Returns a list of notifications for the user's dashboard.
     * - We map the Entity to a {@link NotificationResponseDTO}.
     * - This breaks the "Circular Dependency" where Jackson (JSON library)
     * would try to infinitely serialize User -> Notification -> User.
     * - Sorting is handled at the Database level (OrderByCreatedDateDesc)
     * to ensure the latest news is always on top.
     */


    @Override
    public List<NotificationResponseDTO> getNotificationsByUserId(UUID userId) {
       List<Notification> notifications=notificationRepo.findAllByUserUserIdOrderByCreatedDateDesc(userId);
       return notifications.stream().map(
               n->NotificationResponseDTO.builder().id(n.getNotificationId())
                       .message(n.getMessage())
                       .category(n.getCategory())
                       .status(n.getStatus())
                       .courseId(n.getCourseId())
                       .createdDate(n.getCreatedDate())
                       .build())
               .toList();

    }

    /**
     * AUTOMATED MAINTENANCE (Cron Job)
     * * A scheduled task that runs every Sunday at 1:00 AM.
     * *  DESIGN CHOICE:
     * - Deletes records older than 30 days to keep the 'notification' table
     * from bloating and slowing down the app.
     * - @Transactional is required here because DELETE is a modifying operation.
     */

    @Override
    @Transactional
    @Scheduled(cron = "0 0 1 * * SUN")
    public void cleanOldNotifications() {
        LocalDateTime thirtyDaysAgo=LocalDateTime.now().minusDays(30);
        log.info("Starting notification clean ups for records older than {}",thirtyDaysAgo);
        notificationRepo.deleteNotificationsOlderThan(thirtyDaysAgo);
        log.info("CLEAN UP SUCCESSFUL: ");
    }
}
