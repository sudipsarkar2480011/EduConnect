package com.educonnect.service.implementation.attachment;

import com.educonnect.dto.attachment.AttachmentStreamDTO;
import com.educonnect.exception.custom_exceptions.DocumentExceptions;
import com.educonnect.exception.custom_exceptions.ResourceNotFoundException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.assessment.Assessment;
import com.educonnect.model.assessment.AssignmentAttachment;
import com.educonnect.model.assessment.Submission;
import com.educonnect.model.document.attachment.Attachment;
import com.educonnect.model.user.Student;
import com.educonnect.repo.StudentRepo;
import com.educonnect.repo.assessment.AssessmentRepo;
import com.educonnect.repo.assessment.SubmissionRepo;
import com.educonnect.repo.attachment.AssignmentAttachmentRepo;
import com.educonnect.repo.attachment.AttachmentRepo;
import com.educonnect.service.contract.attachment.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepo attachmentRepo;
    private final AssignmentAttachmentRepo assignmentAttachmentRepo;
    private final StudentRepo studentRepo;
    private final AssessmentRepo assessmentRepo;
    private final SubmissionRepo submissionRepo;

    @Override
    public AttachmentStreamDTO getAttachment(UUID attachmentId) throws DocumentExceptions {

        Attachment attachment = attachmentRepo
                .findById(attachmentId)
                .orElseThrow(() -> new DocumentExceptions("Attachment not found"));

        byte[] fileData = attachment.getFileData();

        if (fileData == null) {
            throw new DocumentExceptions("Document has no data");
        }

        InputStream inputStream = new ByteArrayInputStream(fileData) ;

        return new AttachmentStreamDTO(attachment,inputStream);

    }

    @Override
    public List<String> getAllAttachmentUrisByStudentAndAssessment(UUID studentId, UUID assessmentId) {

        Student student = null;
        try {
            student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new UserNotFoundException("Student not found"));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        Assessment assessment = assessmentRepo.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("assessment not found"));

        Submission submission = submissionRepo.findByStudentAndAssessment(student,assessment)
                .orElseThrow(() -> new ResourceNotFoundException("submission not found"));

        List<AssignmentAttachment>  assignmentAttachmentList = assignmentAttachmentRepo
                .findAllBySubmissionSubmissionId(submission.getSubmissionId());

        List<String> res = new ArrayList<>();

        for (AssignmentAttachment assignmentAttachment : assignmentAttachmentList ) {
            res.add(assignmentAttachment.getUri());
        }

        return res;

    }


}
