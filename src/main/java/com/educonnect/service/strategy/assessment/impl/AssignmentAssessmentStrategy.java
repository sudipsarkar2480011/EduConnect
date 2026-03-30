package com.educonnect.service.strategy.assessment.impl;

import com.educonnect.dto.assessment.report.AssessmentReportDTO;
import com.educonnect.dto.assessment.report.assignment.StudentAssignmentReportDTO;
import com.educonnect.dto.assessment.serve.AssessmentServeDTO;
import com.educonnect.dto.assessment.serve.assignment.AssignmentServeDTO;
import com.educonnect.dto.assessment.submit.AssessmentRequestDTO;
import com.educonnect.dto.assessment.submit.assignment.AssignmentRequestDTO;
import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import com.educonnect.dto.assessment.create.assignment.CreateAssignmentRequestDTO;
import com.educonnect.exception.custom_exceptions.DocumentProcessingException;
import com.educonnect.exception.custom_exceptions.ResourceNotFoundException;
import com.educonnect.model.assessment.*;
import com.educonnect.model.course.Course;
import com.educonnect.model.document.FileTypeEnum;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import com.educonnect.repo.EnrollmentRepo;
import com.educonnect.repo.assessment.AssessmentRepo;
import com.educonnect.repo.assessment.assignment.AssignmentRepo;
import com.educonnect.repo.assessment.SubmissionRepo;
import com.educonnect.repo.attachment.AssignmentAttachmentRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.strategy.assessment.AssessmentStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service

public class AssignmentAssessmentStrategy implements AssessmentStrategy {

    private final AssessmentRepo assessmentRepo;
    private final AssignmentRepo assignmentRepo;
    private final AssignmentAttachmentRepo assignmentAttachmentRepo;
    private final SubmissionRepo submissionRepo;
    private final CourseRepo courseRepo;
    private final EnrollmentRepo enrollmentRepo;

    @Value("${app.attachment.base-url}")
    private String attachmentBaseUrl;

    private final Map<String, FileTypeEnum> allowedTypes =
            new HashMap<>(Map.of(
                    ".pdf",  FileTypeEnum.PDF,
                    ".jpeg", FileTypeEnum.JPEG,
                    ".jpg",  FileTypeEnum.JPEG
            ));

    private FileTypeEnum getFileType(String filename) {
        if (filename == null || filename.isBlank()) {
            return FileTypeEnum.BYTE_STREAM;
        }
        String extension = filename.toLowerCase().substring(filename.lastIndexOf("."));
        return allowedTypes.getOrDefault(extension, FileTypeEnum.BYTE_STREAM);
    }

    @Override
    public boolean supports(AssessmentType type) {
        return type.toString().equals("ASSIGNMENT");
    }

    @Override
    @Transactional
    public Map<String, String> submitAssessment(Student student, AssessmentRequestDTO dto) throws BadRequestException, DocumentProcessingException {

        Assessment assessment = assessmentRepo.findById(dto.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));

        if (!enrollmentRepo.existsByStudentUserIdAndCourseCourseId(
                student.getUserId(), assessment.getCourse().getCourseId())) {
            throw new BadRequestException(
                    "Student `" + student.getFullName() + "` did not enroll to the course!!");
        }

        List<MultipartFile> files = ((AssignmentRequestDTO) dto).getFiles();

        if (files == null || files.isEmpty()) {
            throw new DocumentProcessingException("No files were attached to the submission.");
        }

        Assignment assignment = assignmentRepo.findById(((AssignmentRequestDTO) dto).getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        int uploadCount = files.size();
        int requiredCount = assignment.getNoOfDocumentsToBeUploaded();

        if (uploadCount != requiredCount) {
            if (uploadCount > requiredCount) {
                throw new DocumentProcessingException(
                        "Maximum of " + requiredCount + " attachments allowed.");
            } else {
                throw new DocumentProcessingException(
                        "Please upload " + (requiredCount - uploadCount) + " more attachments.");
            }
        }

        if (submissionRepo.existsByStudentAndAssessment(student, assessment)) {
            throw new BadRequestException("You have already submitted this assignment.");
        }
        Submission submission = Submission.builder()
                .assessment(assessment)
                .student(student)
                .submissionStatus(SubmissionStatus.SUBMITTED)
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

                String originalFilename = file.getOriginalFilename();
                attachment.setFileName(originalFilename);
                attachment.setFileTypeEnum(getFileType(originalFilename));

                String uri = attachmentBaseUrl + attachmentId;
                attachment.setUri(uri);

                attachmentList.add(attachment);

            } catch (Exception e) {
                log.error("Failed to read file: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("Failed to process file: " + file.getOriginalFilename(), e);
            }
        }
        assignmentAttachmentRepo.saveAll(attachmentList);

        Map<String, String> map = new HashMap<>();
        map.put("message", "Assignment submitted successfully");
        map.put("submissionId", submission.getSubmissionId().toString());

        return map;
    }

    @Override
    @Transactional
    public Map<String, String> createAssessment(Teacher teacher, CreateAssessmentRequestDTO dto) throws BadRequestException {
    Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!canCreateAssessment(teacher, course)) {
            throw new BadRequestException("Teacher " + teacher.getFullName()
                    + " can't add assessment to this course: " + course.getTitle());
        }

        Assessment assessment = new Assessment();

        assessment.setCourse(course);
        assessment.setTitle(dto.getTitle());
        assessment.setType(dto.getAssessmentType());
        assessment.setMaxScore(dto.getMaxScore());

        Assignment assignment = new Assignment();

        assignment.setDueDate(dto.getDueDate());
        assignment.setNoOfDocumentsToBeUploaded(
                ((CreateAssignmentRequestDTO) dto).getNoOfDocumentsToBeUploaded() == null
                        ? 10
                        : ((CreateAssignmentRequestDTO) dto).getNoOfDocumentsToBeUploaded()
        );
        assignment.setInstruction(((CreateAssignmentRequestDTO) dto).getInstruction());
        assignment.setAssessment(assessment);
        assessment.setAssignment(assignment);

        assessmentRepo.save(assessment);
        assignmentRepo.save(assignment);

        Map<String, String> map = new HashMap<>();

        map.put("message", "Created Assessment of type " + dto.getAssessmentType().toString());
        map.put("assessmentId", assessment.getAssessmentId().toString());
        map.put("assignmentId", assignment.getAssignmentId().toString());

        return map;
    }

    @Override
    public AssessmentServeDTO serveAssessment(UUID assessmentId, User user) throws BadRequestException {
      Assignment assignment = assignmentRepo.findAssignmentAndAssessment(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        Assessment assessment = assignment.getAssessment();


        if (user.getRole().equals(Role.STUDENT) &&
                !enrollmentRepo.existsByStudentUserIdAndCourseCourseId(
                        user.getUserId(), assessment.getCourse().getCourseId())) {
            throw new BadRequestException(
                    "Student `" + user.getFullName() + "` did not enroll to the course");
        }

        AssignmentServeDTO assignmentServeDTO = new AssignmentServeDTO();

        assignmentServeDTO.setInstruction(assignment.getInstruction());
        assignmentServeDTO.setTitle(assessment.getTitle());
        assignmentServeDTO.setNoOfDocumentsToBeUploaded(assignment.getNoOfDocumentsToBeUploaded());
        assignmentServeDTO.setAssessmentType(AssessmentType.ASSIGNMENT);

        return assignmentServeDTO;
    }

    @Override
    public AssessmentReportDTO getReport(UUID submissionId, User user) throws BadRequestException {

        Submission submission = submissionRepo.findAssignmentAndAssessmentAndAttachments(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        if(user.getRole().equals(Role.STUDENT)
                && !submission.getStudent().getUserId().equals(user.getUserId())
                ){
            throw new BadRequestException("Student " + user.getFullName()
                    + " is not authorized to access this report");
        }

        StudentAssignmentReportDTO assignmentReportDTO = new StudentAssignmentReportDTO();

        List<String> uriList = new ArrayList<>();
        for (AssignmentAttachment assignmentAttachment : submission.getAssignmentAttachmentList()) {
            uriList.add(assignmentAttachment.getUri());
        }
        assignmentReportDTO.setAttachmentUriList(uriList);

        assignmentReportDTO.setTitle(submission.getAssessment().getTitle());
        assignmentReportDTO.setAssessmentType(AssessmentType.ASSIGNMENT);

        assignmentReportDTO.setNoOfDocumentsUploaded(submission.getAssignmentAttachmentList().size());

        return assignmentReportDTO;

    }
}
