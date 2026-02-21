package com.educonnect.repo;

import com.educonnect.model.user.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ParentRepo extends JpaRepository<Parent,Long> {
    List<Parent> findByParentUuidIn(List<UUID> uuids);
}
