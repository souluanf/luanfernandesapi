package dev.luanfernandes.dto.request;

import dev.luanfernandes.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "Request para registro de novo usuário")
@Builder
public record UserRegisterRequest(
        @Schema(description = "Nome do usuário", example = "João Silva") @NotBlank(message = "Nome é obrigatório")
                String name,
        @Schema(description = "Email do usuário", example = "newuser@example.com")
                @NotBlank(message = "Email é obrigatório")
                @Email(message = "Email deve ter formato válido")
                String email,
        @Schema(
                        description = "Perfil do usuário",
                        example = "USER",
                        allowableValues = {"ADMIN", "USER"})
                @NotNull(message = "Role é obrigatório")
                UserRole role) {}
