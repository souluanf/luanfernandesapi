package dev.luanfernandes.dto.request;

import dev.luanfernandes.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Request para atualização de usuário")
public record UserUpdateRequest(
        @Schema(description = "Nome do usuário", example = "João Silva") String name,
        @Schema(description = "Email do usuário", example = "user@example.com")
                @Email(message = "Email deve ter formato válido")
                String email,
        @Schema(
                        description = "Perfil do usuário",
                        examples = {"ADMIN", "USER"},
                        allowableValues = {"ADMIN", "USER"})
                UserRole role) {}
