package com.zoonosys.controllers;

import com.zoonosys.dtos.RegisterNewsDTO;
import com.zoonosys.models.News;
import com.zoonosys.models.User;
import com.zoonosys.services.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.zoonosys.security.userdetails.UserDetailsImpl;
import com.zoonosys.models.User;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping("/register")
    public ResponseEntity<News> createNews(
    @RequestBody @Valid
    RegisterNewsDTO registerNewsDTO,
    @AuthenticationPrincipal UserDetailsImpl authenticatedUserDetails) {
        User authenticatedUser = authenticatedUserDetails.getUser();
        News createdNews = newsService.register(registerNewsDTO, authenticatedUser);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> news = newsService.findAll();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.findById(id);

        return news.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
