package com.zoonosys.dtos;

public record UserResponseDTO(
    Long id,
    String email,
    String name,
    String phone
) {}

