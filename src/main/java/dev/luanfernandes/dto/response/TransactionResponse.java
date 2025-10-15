package dev.luanfernandes.dto.response;

import dev.luanfernandes.domain.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Response com dados da transação")
public record TransactionResponse(
        @Schema(description = "ID da transação") Integer id,
        @Schema(description = "Tipo da transação") TransactionType type,
        @Schema(description = "Categoria") String category,
        @Schema(description = "Valor") BigDecimal amount,
        @Schema(description = "Data da transação") LocalDate transactionDate,
        @Schema(description = "Descrição") String description,
        @Schema(description = "ID do usuário") String userId,
        @Schema(description = "Data de criação") LocalDateTime createdAt,
        @Schema(description = "Data de atualização") LocalDateTime updatedAt) {}
