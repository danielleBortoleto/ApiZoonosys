package com.zoonosys.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoonosys.demo.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String email);
}
