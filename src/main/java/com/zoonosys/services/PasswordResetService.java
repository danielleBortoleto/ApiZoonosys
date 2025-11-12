package com.zoonosys.services;

import com.zoonosys.dtos.ResetPasswordConfirmationDTO;
import com.zoonosys.dtos.ResetPasswordRequestDTO;
import com.zoonosys.models.PasswordResetToken;
import com.zoonosys.models.User;
import com.zoonosys.repositories.PasswordResetTokenRepository;
import com.zoonosys.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private static final long EXPIRATION_MINUTES = 10;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void createAndSendResetToken(ResetPasswordRequestDTO request) {
        Optional<User> userOptional = userRepository.findByEmail(request.email());

        if(userOptional.isEmpty()) {
            logger.warn("Tentativa de reset de senha para e-mail inexistente: {}", request.email());
            return;
        }

        User user = userOptional.get();

        tokenRepository.deleteAllByUserAndUsedIsFalse(user);

        String tokenValue = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenValue);
        token.setUser(user);
        token.setExpiryDate(expiryDate);

        tokenRepository.save(token);
        logger.info("Token de reset gerado e salvo para usuário: {}", user.getEmail());

        try{
            emailService.sendPasswordResetEmail(
                    user.getEmail(),
                    user.getName(),
                    tokenValue
            );
            logger.info("E-mail de reset enviado com sucesso para: {}", user.getEmail());
        } catch (Exception e){
            logger.error("Falha ao enviar e-mail de reset para: {}", user.getEmail(), e);
        }
    }

    /**
     * Verifica se o token existe, não está expirado e não foi usado.
     * @param tokenValue O valor do token recebido da URL.
     * @return O objeto PasswordResetToken se for válido, ou Optional.empty() caso contrário.
     */
    public Optional<PasswordResetToken> validateToken(String tokenValue){
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(tokenValue);

        if(optionalToken.isEmpty()){
            logger.warn("Validação de token falhou: Token '{}' já foi utilizado.", tokenValue);
            return Optional.empty();
        }

        PasswordResetToken token = optionalToken.get();

        if(token.isExpired()){
            logger.warn("Validação de token falhou: Token '{}' expirou em {}.", tokenValue, token.getExpiryDate());
            return Optional.empty();
        }
        logger.info("Token '{}' validado com sucesso.", tokenValue);
        return optionalToken;
    }

    /**
     * Confirma a redefinição de senha: valida o token, verifica senhas, atualiza e invalida o token.
     * @param tokenValue O token de reset de senha recebido da URL.
     * @param newPassword A nova senha.
     * @param confirmationPassword A confirmação da nova senha.
     * @return O objeto User com a senha atualizada (opcional).
     */
    @Transactional
    public Optional<User> resetPassword(String tokenValue, String newPassword, String confirmationPassword) {

        if (!newPassword.equals(confirmationPassword)) {
            logger.warn("Redefinição falhou: Novas senhas não coincidem.");
            return Optional.empty();
        }

        Optional<PasswordResetToken> optionalToken = validateToken(tokenValue);

        if (optionalToken.isEmpty()) {
            logger.warn("Redefinição falhou: Token inválido, expirado ou já usado.");
            return Optional.empty();
        }

        PasswordResetToken token = optionalToken.get();
        User user = token.getUser();

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            logger.warn("Redefinição falhou: A nova senha é idêntica à senha atual para o usuário: {}", user.getEmail());
            return Optional.empty();
        }

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        tokenRepository.deleteAllByUserAndUsedIsFalse(user);

        logger.info("Senha redefinida com sucesso e token invalidado para o usuário: {}", user.getEmail());

        return Optional.of(user);
    }
}
