package com.zoonosys.dtos;

import com.zoonosys.enums.RoleName;

import java.util.Optional;

public record RegisterUserDTO(
        String email,
        String password,
        RoleName role,
        String name,
        String cpf,
        String telefone,
        Optional<String> telefoneSecundario,
        Optional<String> emailSecundario,
        Optional<String> endereco
    ){
}
