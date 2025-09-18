package com.zoonosys.security.config;

import com.zoonosys.enums.RoleName;
import com.zoonosys.models.Role;
import com.zoonosys.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedRoles(RoleRepository roleRepository) {
        return args -> {
            if(roleRepository.findByName(RoleName.ROLE_CUSTOMER)==null){
                roleRepository.save(Role.builder().name(RoleName.ROLE_CUSTOMER).build());
            }
            if(roleRepository.findByName(RoleName.ROLE_ADMINISTRATOR)==null){
                roleRepository.save(Role.builder().name(RoleName.ROLE_ADMINISTRATOR).build());
            }
       };
    }
}
