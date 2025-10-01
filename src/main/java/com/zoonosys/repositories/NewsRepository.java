package com.zoonosys.repositories;

import com.zoonosys.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    Optional<News> findById(Long id);

    List<News> findAll();

    List<News> findByUserId(long userId);

    List<News> findByTitleContainingIgnoreCase(String title);
}
