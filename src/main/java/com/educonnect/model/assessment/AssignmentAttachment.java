package com.educonnect.model.assessment;

import com.educonnect.model.document.attachment.Attachment;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "assignment_attachment_id")

public class AssignmentAttachment extends Attachment {

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

}
