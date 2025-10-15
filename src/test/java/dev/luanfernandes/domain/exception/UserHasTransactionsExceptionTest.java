package dev.luanfernandes.domain.exception;

import static dev.luanfernandes.domain.enums.ExceptionType.CONFLICT;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for UserHasTransactionsException")
class UserHasTransactionsExceptionTest {

    @Test
    @DisplayName("Should create exception with user ID message")
    void shouldCreateException_WithUserIdMessage() {
        var userId = 123;

        var exception = new UserHasTransactionsException(userId);

        assertThat(exception.getMessage()).contains("123");
        assertThat(exception.getMessage()).contains("Não é possível deletar o usuário com id");
        assertThat(exception.getMessage()).contains("pois existem transações associadas");
        assertThat(exception.getErrorCode()).isEqualTo("USER_HAS_TRANSACTIONS");
        assertThat(exception.getHttpStatusCode()).isEqualTo(409);
        assertThat(exception.getType()).isEqualTo(CONFLICT);
    }

    @Test
    @DisplayName("Should have correct error properties")
    void shouldHaveCorrectErrorProperties() {
        var exception = new UserHasTransactionsException(1);

        assertThat(exception).isInstanceOf(BusinessException.class).isInstanceOf(RuntimeException.class);
        assertThat(exception.getErrorCode()).isNotNull();
        assertThat(exception.getHttpStatusCode()).isPositive();
        assertThat(exception.getType()).isNotNull();
    }

    @Test
    @DisplayName("Should create exception with different user IDs")
    void shouldCreateException_WithDifferentUserIds() {
        var exception1 = new UserHasTransactionsException(1);
        var exception2 = new UserHasTransactionsException(999);

        assertThat(exception1.getMessage()).contains("1");
        assertThat(exception2.getMessage()).contains("999");
        assertThat(exception1.getErrorCode()).isEqualTo(exception2.getErrorCode());
        assertThat(exception1.getHttpStatusCode()).isEqualTo(exception2.getHttpStatusCode());
    }

    @Test
    @DisplayName("Should have HTTP status 409 Conflict")
    void shouldHaveHttpStatus409Conflict() {
        var exception = new UserHasTransactionsException(1);

        assertThat(exception.getHttpStatusCode()).isEqualTo(409);
        assertThat(exception.getType()).isEqualTo(CONFLICT);
    }
}
