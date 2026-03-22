package com.educonnect.repo;

import com.educonnect.model.notification.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * WHAT IS THIS?
 * This is the Repository layer that talks directly to the Database.
 * It uses Spring Data JPA to turn Java methods into SQL queries.
 * *KEY OPTIMIZATIONS:
 * 1. Efficient Updates: Instead of loading every notification into Java and
 * changing a boolean (which is slow), it uses a single SQL UPDATE command.
 * 2. Efficient Deletion: It allows for "Bulk Deletion" of old data without
 * looping, which keeps the database clean and fast.
 * 3. Sorting: It automatically handles "Newest First" logic using
 * OrderByCreatedDateDesc.
 * "I used @Modifying and @Transactional for updates and deletes. This ensures
 * the database handles the heavy lifting in a single transaction, rather
 * than pulling thousands of records into the application's memory."
 */

public interface NotificationRepo extends JpaRepository<Notification, UUID> {

    /**
     * Finds all notifications for a specific user.
     */
    List<Notification> findByUser_UserId(UUID userId);

    /**
     * ⚡ BULK UPDATE: Marks all 'unread' notifications as 'read' in one shot.
     * @Modifying - Tells Spring this is an UPDATE/DELETE, not a SELECT.
     * @Param - Safely maps the Java 'userId' to the SQL ':userId'.
     */
    @Modifying
    @Query("UPDATE Notification n SET n.status = true WHERE n.user.userId = :userId AND n.status = false")
    void markAllAsReadByUserId(@Param("userId") UUID userId);

    /**
     * Gets the user's notification feed, sorted so they see the newest alerts at the top.
     */
    List<Notification> findAllByUserUserIdOrderByCreatedDateDesc(UUID userId);

    /**
     * HOUSEKEEPING: Deletes old records based on a timestamp.
     * This is used by the Scheduled Cleanup Task to prevent the DB from
     * growing too large over time.
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdDate < :cutoffDate")
    void deleteNotificationsOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
}

