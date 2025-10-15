package dev.luanfernandes.domain.exception;

import static dev.luanfernandes.domain.enums.ExceptionType.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for TransactionAccessDeniedException")
class TransactionAccessDeniedExceptionTest {

    @Test
    @DisplayName("Should create exception with transaction ID message")
    void shouldCreateException_WithTransactionIdMessage() {
        var transactionId = 789;

        var exception = new TransactionAccessDeniedException(transactionId);

        assertThat(exception.getMessage()).contains("789");
        assertThat(exception.getMessage()).contains("Acesso negado à transação com id");
        assertThat(exception.getErrorCode()).isEqualTo("TRANSACTION_NOT_FOUND");
        assertThat(exception.getHttpStatusCode()).isEqualTo(404);
        assertThat(exception.getType()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("Should have correct error properties")
    void shouldHaveCorrectErrorProperties() {
        var exception = new TransactionAccessDeniedException(1);

        assertThat(exception).isInstanceOf(BusinessException.class).isInstanceOf(RuntimeException.class);
        assertThat(exception.getErrorCode()).isNotNull();
        assertThat(exception.getHttpStatusCode()).isPositive();
        assertThat(exception.getType()).isNotNull();
    }

    @Test
    @DisplayName("Should create exception with different transaction IDs")
    void shouldCreateException_WithDifferentTransactionIds() {
        var exception1 = new TransactionAccessDeniedException(1);
        var exception2 = new TransactionAccessDeniedException(999);

        assertThat(exception1.getMessage()).contains("1");
        assertThat(exception2.getMessage()).contains("999");
        assertThat(exception1.getErrorCode()).isEqualTo(exception2.getErrorCode());
        assertThat(exception1.getHttpStatusCode()).isEqualTo(exception2.getHttpStatusCode());
    }

    @Test
    @DisplayName("Should use TRANSACTION_NOT_FOUND error code")
    void shouldUseTransactionNotFoundErrorCode() {
        var exception = new TransactionAccessDeniedException(1);

        assertThat(exception.getErrorCode()).isEqualTo("TRANSACTION_NOT_FOUND");
        assertThat(exception.getHttpStatusCode()).isEqualTo(404);
    }
}
