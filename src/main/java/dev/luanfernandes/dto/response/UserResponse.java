package dev.luanfernandes.dto.response;

import dev.luanfernandes.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response com dados do usuário")
public record UserResponse(
        @Schema(description = "ID do usuário", example = "1") Integer id,
        @Schema(description = "Nome do usuário", example = "João Silva") String name,
        @Schema(description = "Email do usuário", example = "joao.silva@example.com") String email,
        @Schema(
                        description = "Perfil do usuário",
                        example = "USER",
                        allowableValues = {"USER", "ADMIN"})
                UserRole role,
        @Schema(description = "Data de criação") LocalDateTime createdAt,
        @Schema(description = "Data de atualização") LocalDateTime updatedAt) {}
