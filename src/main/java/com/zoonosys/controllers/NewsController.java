package com.zoonosys.controllers;

import com.zoonosys.dtos.RegisterNewsDTO;
import com.zoonosys.models.News;
import com.zoonosys.models.User;
import com.zoonosys.services.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Tag(name = "Notícias", description = "Endpoints para gerenciamento e consulta de notícias públicas.")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(
            summary = "Registrar uma nova notícia",
            description = "Cria uma nova notícia. Requer token JWT e a autoridade 'ROLE_ADMINISTRATOR'.",
            tags = {"Notícias", "Administração"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Notícia registrada com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = News.class))),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)."),
                    @ApiResponse(responseCode = "403", description = "Proibido (Usuário sem 'ROLE_ADMINISTRATOR')."),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (Erro de validação do DTO).")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<News> createNews(
    @RequestBody @Valid
    RegisterNewsDTO registerNewsDTO,
    @AuthenticationPrincipal UserDetailsImpl authenticatedUserDetails) {
        User authenticatedUser = authenticatedUserDetails.getUser();
        News createdNews = newsService.register(registerNewsDTO, authenticatedUser);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar todas as notícias",
            description = "Retorna uma lista de notícias paginada. Acesso público e ordenado pelas mais recentes, por padrão.",
            tags = {"Notícias"},
            parameters = {
                    @Parameter(name = "page", description = "Número da página a ser buscada (inicia em 0).", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de itens por página.", example = "10"),
                    @Parameter(name = "sort", description = "Propriedade e direção da ordenação (ex: createdAt,desc).", example = "createdAt,desc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de notícias paginada retornada com sucesso."
                            // O schema para Page<News> é complexo e geralmente inferido automaticamente
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<News>> getAllNews(Pageable pageable) {
        Page<News> newsPage = newsService.findAll(pageable);
        return new ResponseEntity<>(newsPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Buscar notícia por ID",
            description = "Retorna uma notícia específica com base no ID fornecido no path. Acesso público.",
            tags = {"Notícias"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Notícia encontrada.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = News.class))),
                    @ApiResponse(responseCode = "404", description = "Notícia não encontrada.")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.findById(id);

        return news.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public ResponseEntity<List<News>> searchNewsByTitle(
            @RequestParam (name = "title") String title) {

        List<News> newsList = newsService.findByTitleContainingIgnoreCase(title);
        return new ResponseEntity<>(newsList, HttpStatus.OK);
    }
}
