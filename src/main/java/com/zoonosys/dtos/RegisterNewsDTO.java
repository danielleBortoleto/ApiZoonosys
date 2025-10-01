package com.zoonosys.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record RegisterNewsDTO (
    @NotBlank(message = "O título não pode estar vazio")
    @Size(min = 3, max = 255, message = "O título deve ter entre 3 e 255 caracteres")
    String title,

    @NotBlank(message = "O conteúdo não pode estar vazio")
    @Size(min = 10, message = "O conteúdo deve ter pelo menos 10 caracteres")
    String content,

    Optional<String> imageUrl
){}
