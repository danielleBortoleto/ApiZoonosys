package com.zoonosys.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

@Schema(description = "Estrutura de dados para registrar uma nova notícia.")
public record RegisterNewsDTO (

    @Schema(description = "Título da notícia. Campo obrigatório.", example = "Campanha de Vacinação em Massa")
    @NotBlank(message = "O título não pode estar vazio")
    @Size(min = 3, max = 255, message = "O título deve ter entre 3 e 255 caracteres")
    String title,

    @Schema(description = "Conteúdo principal da notícia. Suporta tags HTML básicas.", example = "<p>Leia atentamente as novas regras.</p>")
    @NotBlank(message = "O conteúdo não pode estar vazio")
    @Size(min = 10, message = "O conteúdo deve ter pelo menos 10 caracteres")
    String content,

    Optional<String> imageUrl
){}
