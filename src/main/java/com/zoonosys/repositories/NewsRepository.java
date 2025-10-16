package com.zoonosys.repositories;

import com.zoonosys.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByUserId(long userId);

    List<News> findByTitleContainingIgnoreCase(String title);
}
