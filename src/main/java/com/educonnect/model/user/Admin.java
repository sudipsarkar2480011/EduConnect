package com.educonnect.model.user;

import java.util.List;
import java.util.UUID;

import com.educonnect.model.audit.Audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name="admin_id")
@Data
public class Admin extends User{
    @Column(columnDefinition = "BINARY(16)")
    private UUID adminUuid = UUID.randomUUID();

    @OneToMany(mappedBy = "admin")
    private List<Audit> audits;

}
