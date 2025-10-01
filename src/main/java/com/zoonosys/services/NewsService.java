package com.zoonosys.services;

import com.zoonosys.dtos.RegisterNewsDTO;
import com.zoonosys.models.News;
import com.zoonosys.models.User;
import com.zoonosys.repositories.NewsRepository;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    private static final PolicyFactory SANITIZER_POLICY = new HtmlPolicyBuilder()
            .allowElements("p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "li", "blockquote", "pre", "br", "div")
            .allowElements("b", "i", "strong", "em", "a", "span", "code", "img")
            .allowAttributes("href").onElements("a")
            .allowAttributes("src", "alt", "title", "width", "height").onElements("img")
            .allowAttributes("class").onElements("p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "li", "div", "span") // Exemplo: permite classes para estilização
            .toFactory();

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public News register(RegisterNewsDTO registerNewsDTO, User authenticatedUser) {
        News news = News.builder()
                .title(registerNewsDTO.title())
                .content(registerNewsDTO.content())
                .imageUrl(registerNewsDTO.imageUrl().orElse(null))
                .build();

        news.setUser(authenticatedUser);
        news.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        String sanitizedTitle = SANITIZER_POLICY.sanitize(news.getTitle());
        String sanitizedContent = SANITIZER_POLICY.sanitize(news.getContent());

        news.setTitle(sanitizedTitle);
        news.setContent(sanitizedContent);

        return newsRepository.save(news);
    }

    public List<News> findAll() {
        return newsRepository.findAll();
    }

    public Optional<News> findById(Long id) {
        return newsRepository.findById(id);
    }

//    public List<News> findByTitleContainingIgnoreCase(String title) {
//        return newsRepository.findByTitleContainingIgnoreCase(title);
//    }
}
