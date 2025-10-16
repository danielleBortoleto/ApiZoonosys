package com.zoonosys.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.Optional;

@Schema(description = "Estrutura de dados para atualizar uma campanha existente.")
public record UpdateCampaignsDTO(
        @Schema(description = "Novo nove da campanha. Campo Obrigatório.", example = "Atualização: Campanha de Vacinação em Massa")
        @NotBlank(message = "O título não pode estar vazio")
        @Size(min = 3, max = 255, message = "O nome deve ser entre 3 e 255 caracteres")
        String name,

        @Schema(description = "Novo conteúdo principal da campanha. Suporta tags HTML básicas.", example = "<p>Novos detalhes e datas.</p>")
        @NotBlank(message = "O conteúdo não pode estar vazio")
        @Size(min = 3, max = 255, message = "O conteúdo deve ter pelo menos 10 caracteres")
        String description,

        @Schema(description = "Nova data e horário inicial da campanha. Campo Obrigatório", example = "Nova data e horário inicial: dd/mm/aaaa hh:mm:ss")
        @NotBlank(message = "O horário e data de início não pode estar vazio")
        Timestamp startDateTime,

        @Schema(description = "Nova data e horário final da campanha. Campo Opcional.", example = "Nova data e horário final: dd/mm/aaaa hh:mm:ss")
        @NotBlank(message = "O horário e data de início não pode estar vazio")
        Optional<Timestamp> endDateTime,

        Optional<String> imageUrl
) {}
