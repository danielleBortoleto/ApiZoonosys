package com.zoonosys.auth.dtos;

public record LoginUserDTO(
        String email,
        String password
) {
}
