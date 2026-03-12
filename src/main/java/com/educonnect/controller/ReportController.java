package com.educonnect.controller;

import com.educonnect.dto.report.FullSystemReportDTO;
import com.educonnect.service.implementation.report.ReportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportServiceImpl reportService;

    @GetMapping("/all-data")
    public ResponseEntity<FullSystemReportDTO> getFullReport() {
        return ResponseEntity.ok(reportService.getAllDataReport());
    }
}