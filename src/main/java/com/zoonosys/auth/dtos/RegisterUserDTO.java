package com.zoonosys.auth.dtos;

import com.zoonosys.auth.enums.RoleName;

public record RegisterUserDTO(
    String email,
    String password,
    RoleName role
    ){
}
