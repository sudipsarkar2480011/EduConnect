package com.educonnect.controller;

import com.educonnect.dto.attendance.AttendanceResponseDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.service.contract.attendance.AttendanceService;
import com.educonnect.utils.mapper.AttendanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AttendanceMapper attendanceMapper;

    @PostMapping
    public ResponseEntity<Boolean> addAttendance(@RequestParam UUID studentId, @RequestParam UUID courseId) throws UserNotFoundException {
        return ResponseEntity.ok(attendanceService.addAttendance(studentId,courseId));
    }

    @GetMapping("of")
    public  ResponseEntity<List<AttendanceResponseDTO>> findBy(
            @RequestParam (required = false) UUID studentId, @RequestParam(required = false) UUID courseId
    ){
        if(studentId!=null && courseId==null){
            return  ResponseEntity.ok(attendanceMapper.toListResponseDTO(attendanceService.findByStudentId(studentId)));
        }
        if(courseId!=null && studentId==null){
            return  ResponseEntity.ok(attendanceMapper.toListResponseDTO(attendanceService.findByCourseId(courseId)));
        }
        if(courseId!=null && studentId!=null) {
            return ResponseEntity.ok(attendanceMapper.toListResponseDTO(attendanceService.findByStudentIdAndCourseId(studentId, courseId)));
        }
        return  ResponseEntity.badRequest().build();
    }
}
