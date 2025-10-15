package dev.luanfernandes.domain.exception;

import static dev.luanfernandes.domain.enums.ExceptionType.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for TransactionNotFoundException")
class TransactionNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with transaction ID message")
    void shouldCreateException_WithTransactionIdMessage() {
        var transactionId = 456;

        var exception = new TransactionNotFoundException(transactionId);

        assertThat(exception.getMessage()).contains("456");
        assertThat(exception.getMessage()).contains("Transação não encontrada com id:");
        assertThat(exception.getErrorCode()).isEqualTo("TRANSACTION_NOT_FOUND");
        assertThat(exception.getHttpStatusCode()).isEqualTo(404);
        assertThat(exception.getType()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("Should have correct error properties")
    void shouldHaveCorrectErrorProperties() {
        var exception = new TransactionNotFoundException(1);

        assertThat(exception).isInstanceOf(BusinessException.class).isInstanceOf(RuntimeException.class);
        assertThat(exception.getErrorCode()).isNotNull();
        assertThat(exception.getHttpStatusCode()).isPositive();
        assertThat(exception.getType()).isNotNull();
    }

    @Test
    @DisplayName("Should create exception with different transaction IDs")
    void shouldCreateException_WithDifferentTransactionIds() {
        var exception1 = new TransactionNotFoundException(1);
        var exception2 = new TransactionNotFoundException(999);

        assertThat(exception1.getMessage()).contains("1");
        assertThat(exception2.getMessage()).contains("999");
        assertThat(exception1.getErrorCode()).isEqualTo(exception2.getErrorCode());
        assertThat(exception1.getHttpStatusCode()).isEqualTo(exception2.getHttpStatusCode());
    }
}
