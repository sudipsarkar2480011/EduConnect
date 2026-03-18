package com.educonnect.repo.report;

import com.educonnect.dto.report.AttendanceStatsDTO;
import com.educonnect.dto.report.ExamStatsDTO;
import com.educonnect.dto.report.GraphDataPointDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public class SystemStatisticsRepo {

    @PersistenceContext
    private EntityManager entityManager;

    public AttendanceStatsDTO getAttendanceStats() {
        String sql = "SELECT " +
                "COUNT(CASE WHEN status = 'PRESENT' THEN 1 END), " +
                "COUNT(CASE WHEN status = 'ABSENT' THEN 1 END), " +
                "CAST(AVG(CASE WHEN status = 'PRESENT' THEN 100.0 ELSE 0.0 END) AS DOUBLE PRECISION) " +
                "FROM attendance";

        Query query = entityManager.createNativeQuery(sql);
        Object[] result = (Object[]) query.getSingleResult();

        return new AttendanceStatsDTO(
                result[0] != null ? ((Number) result[0]).longValue() : 0L,
                result[1] != null ? ((Number) result[1]).longValue() : 0L,
                result[2] != null ? ((Number) result[2]).doubleValue() : 0.0
        );
    }

    public ExamStatsDTO getExamStats() {
        String sql = "SELECT CAST(AVG(score) AS DOUBLE PRECISION), COUNT(result_id), CAST(MAX(score) AS DOUBLE PRECISION) FROM result";

        Query query = entityManager.createNativeQuery(sql);
        Object[] result = (Object[]) query.getSingleResult();

        return new ExamStatsDTO(
                result[0] != null ? ((Number) result[0]).doubleValue() : 0.0,
                result[1] != null ? ((Number) result[1]).longValue() : 0L,
                result[2] != null ? ((Number) result[2]).doubleValue() : 0.0
        );
    }

    public List<GraphDataPointDTO> getStudentPerformanceTrend(UUID studentId) {
        String sql = "SELECT a.date, r.percentage_score " +
                "FROM result r " +
                "JOIN audit a ON r.result_id = a.audit_id " +
                "WHERE r.student_id = :studentId " +
                "ORDER BY a.date ASC";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("studentId", studentId);

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(row -> new GraphDataPointDTO(
                        (LocalDate) row[0],
                        ((Number) row[1]).doubleValue()
                ))
                .toList();
    }

    public List<GraphDataPointDTO> getCoursePerformanceTrend(UUID courseId) {
        String sql = "SELECT a.date, AVG(r.percentage_score) " +
                "FROM result r " +
                "JOIN audit a ON r.result_id = a.audit_id " +
                "GROUP BY a.date " +
                "ORDER BY a.date ASC";

        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(row -> new GraphDataPointDTO(
                        (java.time.LocalDate) row[0],
                        ((Number) row[1]).doubleValue()
                ))
                .toList();
    }
}