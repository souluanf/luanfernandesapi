package dev.luanfernandes.domain.entity;

import dev.luanfernandes.domain.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Transaction {
    private Integer id;
    private TransactionType type;
    private String category;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private String description;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
