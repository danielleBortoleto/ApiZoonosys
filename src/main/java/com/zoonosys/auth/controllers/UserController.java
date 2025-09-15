package com.zoonosys.auth.controllers;

import com.zoonosys.auth.dtos.LoginUserDTO;
import com.zoonosys.auth.dtos.RecoveryJwtTokenDTO;
import com.zoonosys.auth.dtos.RegisterUserDTO;
import com.zoonosys.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDTO> authenticateUser(@RequestBody LoginUserDTO loginUserDTO) {
        RecoveryJwtTokenDTO token = userService.authenticateUser(loginUserDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

   @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterUserDTO registerUserDTO){
        userService.registerUser(registerUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public ResponseEntity<String> getAuthenticationTest(){
        return new ResponseEntity<>("Authenticated successfully", HttpStatus.OK);
    }

    @GetMapping("/test/customer")
    public ResponseEntity<String> getCustomerAuthenticationTest(){
        return new ResponseEntity<>("Client authenticated successfully", HttpStatus.OK);
    }

    @GetMapping("/test/administrator")
    public ResponseEntity<String> getAdministratorAuthenticationTest(){
        return new ResponseEntity<>("Administrator authenticated successfully", HttpStatus.OK);
    }
}
