package com.zoonosys.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.Optional;

@Schema(description = "Estrutura de dados para registrar uma nova campanha.")
public record RegisterCampaignsDTO(
        @Schema(description = "Nome da Campanha. Campo obrigatório.", example = "Campanha de Vacinação em Massa")
        @NotBlank(message = "O nome não pode estar vazio")
        @Size(min = 3, max = 255, message = "O título deve ter entre 3 e 255 caracteres")
        String name,

        @Schema(description = "Descrição da Campanha. Suporta tags HTML básicas.", example = "<p>Leia atentamente as novas regras.</p>")
        @NotBlank(message = "O conteúdo não pode estar vazio")
        @Size(min = 10, message = "O conteúdo deve ter pelo menos 10 caracteres")
        String description,

        @Schema(description = "Data e hora de início da campanha no formato AAAA-MM-DD HH:MM:SS. Campo obrigatório.", example = "2024-07-01 08:00:00")
        @NotBlank(message = "O conteúdo não pode estar vazio")
        Timestamp startDateTime,

        Optional<Timestamp> endDateTime,

        Optional<String> imageUrl
) {}
