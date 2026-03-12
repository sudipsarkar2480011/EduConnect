package com.educonnect.repo.report;

import com.educonnect.model.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportRepo extends JpaRepository<Report, UUID> {

}