package com.educonnect.repo.compliance;

import com.educonnect.model.compliance.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
}
