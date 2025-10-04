package com.zoonosys.services;

import com.zoonosys.dtos.LoginUserDTO;
import com.zoonosys.dtos.RecoveryJwtTokenDTO;
import com.zoonosys.dtos.RegisterUserDTO;
import com.zoonosys.dtos.UserResponseDTO;
import com.zoonosys.models.Role;
import com.zoonosys.models.User;
import com.zoonosys.repositories.RoleRepository;
import com.zoonosys.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.zoonosys.security.authentication.JwtTokenService;
import com.zoonosys.security.userdetails.UserDetailsImpl;

import java.util.List;
import java.util.Optional;

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
            throw new RuntimeException("Role '" + registerUserDTO.role() + "' not found.");
        }

        User newUser = User.builder()
                .email(registerUserDTO.email())
                .password(passwordEncoder.encode(registerUserDTO.password()))
                .name(registerUserDTO.name())
                .cpf(registerUserDTO.cpf())
                .phone(registerUserDTO.phone())
                .roles(List.of(role))
                .secundaryEmail(registerUserDTO.secundaryEmail().orElse(null))
                .secundaryPhone(registerUserDTO.secundaryPhone().orElse(null))
                .sexo(registerUserDTO.sexo().orElse(null))
                .address(registerUserDTO.address().orElse(null))
                .build();

        userRepository.save(newUser);
    }

    public List<UserResponseDTO> findAllUsersDTO() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getEmail(), user.getName(), user.getPhone()))
                .toList();
    }

    public Optional<UserResponseDTO> findUserDTOById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDTO(user.getId(), user.getEmail(), user.getName(), user.getPhone()));
    }


}