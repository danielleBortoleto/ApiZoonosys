package com.zoonosys.auth.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.zoonosys.auth.models.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
