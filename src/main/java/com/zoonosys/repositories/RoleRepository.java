package com.zoonosys.repositories;

import com.zoonosys.enums.RoleName;
import com.zoonosys.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findByName(RoleName name);
}
