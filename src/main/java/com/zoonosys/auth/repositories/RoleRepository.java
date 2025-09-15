package com.zoonosys.auth.repositories;

import com.zoonosys.auth.enums.RoleName;
import com.zoonosys.auth.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findByName(RoleName name);
}
