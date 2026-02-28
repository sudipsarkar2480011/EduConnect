package com.educonnect.repo;

import com.educonnect.model.user.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface ParentRepo extends JpaRepository<Parent,UUID> {
    List<Parent> findByUserIdIn(List<UUID> parentId);
}
