package com.zoonosys.dtos;

import com.zoonosys.enums.RoleName;

import java.util.Optional;

public record RegisterUserDTO(
        String email,
        String password,
        RoleName role,
        String name,
        String cpf,
        String phone,
        Optional<String> sexo,
        Optional<String> secundaryPhone,
        Optional<String> secundaryEmail,
        Optional<String> address
    ){
}
