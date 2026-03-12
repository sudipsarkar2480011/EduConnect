package com.educonnect.utils.mapper;

import com.educonnect.dto.attendance.AttendanceRequestDTO;
import com.educonnect.dto.attendance.AttendanceResponseDTO;
import com.educonnect.model.attendance.Attendance;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttendanceMapper implements  Mapper<Attendance, AttendanceRequestDTO, AttendanceResponseDTO>{
    @Override
    public Attendance toEntity(AttendanceRequestDTO requestDTO) {
        return null;
    }

    @Override
    public AttendanceResponseDTO toResponseDTO(Attendance entity) {
        return new AttendanceResponseDTO(
                entity.getAttendanceId(),
                entity.getStudent().getUserId(),
                entity.getStudent().getEmail(),
                entity.getCourse().getCourseId(),
                entity.getCourse().getTitle(),
                entity.getDate(),
                entity.getStatus()
        );
    }

    public  List<AttendanceResponseDTO> toListResponseDTO(List<Attendance> attendanceList){
        return attendanceList.stream().map(this::toResponseDTO).toList();
    }
}
