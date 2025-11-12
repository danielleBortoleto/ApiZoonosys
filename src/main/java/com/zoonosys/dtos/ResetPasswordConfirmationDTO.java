package com.zoonosys.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public record ResetPasswordConfirmationDTO(
        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
        String newPassword,

        @NotBlank(message = "A confirmação da senha é obrigatória.")
        String confirmationPassword
) {
}
