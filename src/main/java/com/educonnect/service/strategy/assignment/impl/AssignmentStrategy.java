package com.educonnect.service.strategy.assignment.impl;

import com.educonnect.dto.assessment.submit.AssessmentRequestDTO;
import com.educonnect.dto.assessment.submit.assignment.AssignmentRequestDTO;
import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import com.educonnect.dto.assessment.create.assignment.CreateAssignmentRequestDTO;
import com.educonnect.exception.custom_exceptions.DocumentProcessingException;
import com.educonnect.exception.custom_exceptions.ResourceNotFoundException;
import com.educonnect.model.assessment.*;
import com.educonnect.model.course.Course;
import com.educonnect.model.document.FileTypeEnum;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import com.educonnect.repo.assessment.AssessmentRepo;
import com.educonnect.repo.assessment.assignment.AssignmentRepo;
import com.educonnect.repo.assessment.SubmissionRepo;
import com.educonnect.repo.attachment.AssignmentAttachmentRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.strategy.assignment.AssessmentStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

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
    @Transactional
    public String submitAssessment(User user, AssessmentRequestDTO dto)  {

        if (!(user instanceof Student student)) {
            try {
                throw new BadRequestException("Only students can submit assessments.");
            } catch (BadRequestException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }


        List<MultipartFile> files = ((AssignmentRequestDTO)dto).getFiles();

        if (files == null || files.isEmpty()) {
            try {
                throw new DocumentProcessingException("No files were attached to the submission.");
            } catch (DocumentProcessingException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        Assignment assignment = assignmentRepo.findById(((AssignmentRequestDTO)dto).getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        Assessment assessment = assessmentRepo.findById(dto.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));

        int uploadedCount = files.size();
        int requiredCount = assignment.getNoOfDocumentsToBeUploaded();

        if (uploadedCount != requiredCount) {
            if (uploadedCount > requiredCount) {
                try {
                    throw new DocumentProcessingException("Maximum of " + requiredCount + " attachments allowed.");
                } catch (DocumentProcessingException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    throw new DocumentProcessingException("Please upload " + (requiredCount - uploadedCount) + " more attachments.");
                } catch (DocumentProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (submissionRepo.existsByStudentAndAssessment((Student) user, assessment)) {
            try {
                throw new BadRequestException("You have already submitted this assignment.");
            } catch (BadRequestException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        Submission submission = Submission.builder()
                .assessment(assessment)
                .student((Student) user)
                .submissionStatus(SubmissionStatus.NOT_SUBMITTED)
                .build();


        submission = submissionRepo.save(submission);

        List<AssignmentAttachment> attachmentList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {

                AssignmentAttachment attachment = new AssignmentAttachment();

                UUID attachmentId = UUID.randomUUID();
                attachment.setAttachmentId(attachmentId);

                attachment.setAssignment(assignment);
                attachment.setSubmission(submission);
                attachment.setFileData(file.getBytes());
                attachment.setDescription(dto.getDescription());
                attachment.setFileName(file.getOriginalFilename());
                attachment.setFileTypeEnum(getFileType(file.getOriginalFilename()));

                String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/v1/api/attachment/view/")
                        .path(attachmentId.toString())
                        .toUriString();

                attachment.setUri(uri);

                attachmentList.add(attachment);

            } catch (Exception e) {
                log.error("Failed to read file: {}", file.getOriginalFilename(), e);
                throw new RuntimeException(e);
            }
        }

        submission.setSubmissionStatus(SubmissionStatus.SUBMITTED);

        assignmentAttachmentRepo.saveAll(attachmentList);

        return "Assessment submitted successfully";
    }


    @Override
    @Transactional
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
            assignment.setNoOfDocumentsToBeUploaded(
                    ((CreateAssignmentRequestDTO)dto).getNoOfDocumentsToBeUploaded() == null?
                            10 : ((CreateAssignmentRequestDTO)dto).getNoOfDocumentsToBeUploaded()
            );

            assignment.setAssessment(assessment);
            assessment.setAssignment(assignment);

            assessmentRepo.save(assessment);
            assignmentRepo.save(assignment);

            return "Created Assessment of type " + dto.getAssessmentType().toString();
    }
}
