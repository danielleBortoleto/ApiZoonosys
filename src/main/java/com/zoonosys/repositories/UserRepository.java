package com.zoonosys.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.zoonosys.models.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
