package com.zoonosys.controllers;

import com.zoonosys.dtos.LoginUserDTO;
import com.zoonosys.dtos.RecoveryJwtTokenDTO;
import com.zoonosys.dtos.RegisterUserDTO;
import com.zoonosys.dtos.UserResponseDTO;
import com.zoonosys.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name="Users", description = "Endpoints para gerenciamento de usuários.")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(
            summary = "Faz a requisição de login de usuário",
            description = "Autentica o usuário através dos parâmetros de LoginUserDTO e gera o token Jwt. Caso bem executado, retorna o token de retorno para o usuário."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso, retorna o token JWT",
                    content = @Content(schema = @Schema(implementation = RecoveryJwtTokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDTO> authenticateUser(@RequestBody LoginUserDTO loginUserDTO) {
        RecoveryJwtTokenDTO token = userService.authenticateUser(loginUserDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @Operation(
            summary = "Faz a requisição de registro de usuário",
            description = "Registra o usuário através dos parâmetros de RegisterUserDTO"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para registro")
    })
   @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterUserDTO registerUserDTO){
        System.out.println("Controller");
        userService.registerUser(registerUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Busca se a authenticação do usuário"
    )
    @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso")
    @GetMapping("/test")
    public ResponseEntity<String> getAuthenticationTest(){
        return new ResponseEntity<>("Authenticated successfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Busca se a authenticação específica do usuário cliente"
    )
    @ApiResponse(responseCode = "200", description = "Cliente autenticado com sucesso")
    @GetMapping("/test/customer")
    public ResponseEntity<String> getCustomerAuthenticationTest(){
        return new ResponseEntity<>("Client authenticated successfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Busca se a authenticação específica do usuário administrador"
    )
    @ApiResponse(responseCode = "200", description = "Administrador autenticado com sucesso")
    @GetMapping("/test/administrator")
    public ResponseEntity<String> getAdministratorAuthenticationTest(){
        return new ResponseEntity<>("Administrator authenticated successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.findAllUsersDTO();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("list/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.findUserDTOById(id)
                .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
