package com.zoonosys.controllers;

import com.zoonosys.dtos.ResetPasswordConfirmationDTO;
import com.zoonosys.dtos.ResetPasswordRequestDTO;
import com.zoonosys.exceptions.UserNotFoundException;
import com.zoonosys.models.PasswordResetToken;
import com.zoonosys.models.User;
import com.zoonosys.services.PasswordResetService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    @Autowired
    private PasswordResetService resetService;

    @PostMapping("/reset-password/request")
    public ResponseEntity<String> requestPasswordReset(@Valid @RequestBody ResetPasswordRequestDTO request) {

        try {
            resetService.createAndSendResetToken(request);
            return ResponseEntity.ok("Se o e-mail estiver registrado, um link de reset foi enviado.");

        } catch (UserNotFoundException e) {
            logger.warn("Tentativa de reset de senha falhou para e-mail não encontrado: {}", request.email());
            return ResponseEntity.ok("Se o e-mail estiver registrado, um link de reset foi enviado.");

        } catch (Exception e) {
            throw new RuntimeException("Falha na solicitação de reset.");
        }
    }

    /**
     * Endpoint para validar o token recebido no link de e-mail.
     * @param tokenValue de reset de senha.
     * @return 200 OK se válido, 400 Bad Request caso contrário.
     */
    @GetMapping("/reset-password/validate")
    public ResponseEntity<?> validatePasswordResetToken(@RequestParam("token") String tokenValue) {
        Optional<PasswordResetToken> validToken = resetService.validateToken(tokenValue);

        if (validToken.isPresent()) {
            return ResponseEntity.ok().body("Token válido. Prossiga para a redefinição de senha.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token de reset inválido, expirado ou já utilizado.");
        }
    }

    /**
     * Endpoint para confirmar a redefinição de senha.
     * @param confirmation DTO com o token, nova senha e confirmação.
     * @return 200 OK se a senha for redefinida, 400 Bad Request caso contrário.
     */

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> confirmPasswordReset(
            @RequestParam("token") String tokenValue,
            @Valid @RequestBody ResetPasswordConfirmationDTO confirmation
    ) {
        try {
            Optional<User> userOptional = resetService.resetPassword(
                    tokenValue,
                    confirmation.newPassword(),
                    confirmation.confirmationPassword()
            );

            if(userOptional.isPresent()){
                return ResponseEntity.ok("Senha redefinida com sucesso.");
            } else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha na redefinição de senha. Verifique o token ou se as senhas coincidem.");
            }
        }catch (Exception e){
            logger.error("Error inesperado durante a redefinição de senha: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno ao tentar redefinir a senha.");
        }
    }
}
