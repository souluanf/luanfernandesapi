package dev.luanfernandes.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;

@Schema(description = "Response com saldo mensal")
@Builder
public record MonthlyBalanceResponse(
        @Schema(description = "Ano") Integer year,
        @Schema(description = "Mês") Integer month,
        @Schema(description = "Total de receitas") BigDecimal totalIncome,
        @Schema(description = "Total de despesas") BigDecimal totalExpense,
        @Schema(description = "Saldo (receitas - despesas)") BigDecimal balance) {}
