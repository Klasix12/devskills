package com.klasix12.devskills.repository;

import com.klasix12.devskills.dto.RoleName;
import com.klasix12.devskills.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
