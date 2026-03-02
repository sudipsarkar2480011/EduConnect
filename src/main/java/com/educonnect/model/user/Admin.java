package com.educonnect.model.user;

import java.util.List;
import com.educonnect.model.audit.Audit;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name="admin_id")
@Data
public class Admin extends User{

    @OneToMany(mappedBy = "admin")
    private List<Audit> audits;

}
