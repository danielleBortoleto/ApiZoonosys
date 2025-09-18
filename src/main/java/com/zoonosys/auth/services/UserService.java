package com.zoonosys.auth.services;

import com.zoonosys.auth.dtos.LoginUserDTO;
import com.zoonosys.auth.dtos.RecoveryJwtTokenDTO;
import com.zoonosys.auth.dtos.RegisterUserDTO;
import com.zoonosys.auth.enums.RoleName;
import com.zoonosys.auth.models.Role;
import com.zoonosys.auth.models.User;
import com.zoonosys.auth.repositories.RoleRepository;
import com.zoonosys.auth.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.zoonosys.auth.security.authentication.JwtTokenService;
import com.zoonosys.auth.security.config.SecurityConfig;
import com.zoonosys.auth.security.userdetails.UserDetailsImpl;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RecoveryJwtTokenDTO authenticateUser(LoginUserDTO loginUserDTO){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUserDTO.email(), loginUserDTO.password());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new RecoveryJwtTokenDTO(jwtTokenService.generateToken(userDetails));
    }

    @Transactional
    public void registerUser(RegisterUserDTO registerUserDTO) {
        if (userRepository.findByEmail(registerUserDTO.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        Role role = roleRepository.findByName(registerUserDTO.role());
        if (role == null) {
            // Tratar o caso em que a Role não é encontrada (lançar exceção, por exemplo)
            throw new RuntimeException("Role '" + registerUserDTO.role() + "' not found.");
        }

        User newUser = User.builder()
                .email(registerUserDTO.email())
                .password(passwordEncoder.encode(registerUserDTO.password()))
                .name(registerUserDTO.name())
                .cpf(registerUserDTO.cpf())
                .telefone(registerUserDTO.telefone())
                .roles(List.of(role))
                .emailSecundario(registerUserDTO.emailSecundario().orElse(null))
                .telefoneSecundario(registerUserDTO.telefoneSecundario().orElse(null))
                .emailSecundario(registerUserDTO.emailSecundario().orElse(null))
                .build();

        userRepository.save(newUser);
    }

}