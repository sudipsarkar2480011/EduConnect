package com.educonnect.service.strategy.assignment.impl;

import com.educonnect.dto.assessment.AssessmentRequestDTO;
import com.educonnect.dto.assessment.AssignmentRequestDTO;
import com.educonnect.dto.assessment.CreateAssessmentRequestDTO;
import com.educonnect.exception.custom_exceptions.ResourceNotFoundException;
import com.educonnect.model.assessment.*;
import com.educonnect.model.course.Course;
import com.educonnect.model.document.FileTypeEnum;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import com.educonnect.repo.assessment.AssessmentRepo;
import com.educonnect.repo.assessment.AssignmentAttachmentRepo;
import com.educonnect.repo.assessment.AssignmentRepo;
import com.educonnect.repo.assessment.SubmissionRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.strategy.assignment.AssessmentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AssignmentStrategy implements AssessmentStrategy {


    private final AssessmentRepo assessmentRepo;
    private final AssignmentRepo assignmentRepo;
    private final AssignmentAttachmentRepo assignmentAttachmentRepo;
    private final SubmissionRepo submissionRepo;
    private final CourseRepo courseRepo;

    private final Map<String, FileTypeEnum> allowedTypes =
            new HashMap<>(Map.of(
                    ".pdf",FileTypeEnum.PDF,
                    "jpeg",FileTypeEnum.JPEG,
                    "jpg",FileTypeEnum.JPEG
            ));

    private FileTypeEnum getFileType(String filename){
        filename = filename.toLowerCase();
        String extension= filename.substring(filename.lastIndexOf("."));
        return allowedTypes.getOrDefault(extension,FileTypeEnum.BYTE_STREAM);
    }

    @Override
    public boolean supports(AssessmentType type) {
        return type.toString().equals("ASSIGNMENT");
    }

    @Override
    public String submitAssessment(User user,
                                 AssessmentRequestDTO dto
    ) {

        try {

            Assessment assessment = assessmentRepo.findById(dto.getAssessment_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));


            List<MultipartFile> files = ((AssignmentRequestDTO)dto).getFiles();

            Assignment assignment = assignmentRepo.findById(((AssignmentRequestDTO)dto).getAssignment_id())
                    .orElseThrow(()-> new ResourceNotFoundException("Assignment not found"));

            List<AssignmentAttachment> assignmentAttachmentList =

                    files.stream().map(
                            file -> {
                                AssignmentAttachment assignmentAttachment = new AssignmentAttachment();

                                assignmentAttachment.setAssignment(assignment);
                                try {
                                    assignmentAttachment.setFileData(file.getBytes());
                                } catch (IOException e) {
                                    log.error("Error : {}", e.getMessage());
                                }
                                assignmentAttachment.setDescription(dto.getDescription());

                                assignmentAttachment.setFileName(file.getOriginalFilename());

                                assignmentAttachment.setFileTypeEnum(getFileType(file.getOriginalFilename()));

                                return assignmentAttachment;
                            }
                    ).toList();

            assignmentAttachmentRepo.saveAll(assignmentAttachmentList);

            //Syncing
            assignment.setAssignmentAttachmentList(assignmentAttachmentList);
            assessment.setAssignment(assignment);

            if(!user.getRole().toString().equals("STUDENT")){
                throw new InternalException("ERROR !!!");
            }
            Submission submission =
                    Submission.builder()
                            .assessment(assessment)
                            .student((Student) user)
                            .teacher(assessment.getCourse().getTeacher())
                            .build();

            submissionRepo.save(submission);

        } catch (Exception e) {
            log.error("Error submit assessment : {}",e.getMessage());
            throw new RuntimeException(e);
        }

        return "Assessment submitted successfully";
    }

    @Override
    public String createAssessment(Teacher teacher, CreateAssessmentRequestDTO dto) {
        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        //Assignment

        Assessment assessment = new Assessment();

        assessment.setCourse(course);
        assessment.setTitle(dto.getTitle());
        assessment.setType(dto.getAssessmentType());
        assessment.setMaxScore(dto.getMaxScore());

        Assignment assignment = new Assignment();

        assignment.setDueDate(dto.getDueDate());
        assignment.setNoOfAssignmentDone(0);

        assignment.setAssessment(assessment);
        assessment.setAssignment(assignment);

        assessmentRepo.save(assessment);
        assignmentRepo.save(assignment);

        return "Created Assessment of type " + dto.getAssessmentType().toString();
    }
}
