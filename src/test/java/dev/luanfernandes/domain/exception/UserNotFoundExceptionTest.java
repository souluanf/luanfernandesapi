package dev.luanfernandes.domain.exception;

import static dev.luanfernandes.domain.enums.ExceptionType.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for UserNotFoundException")
class UserNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with user ID message")
    void shouldCreateException_WithUserIdMessage() {
        var userId = 123;

        var exception = new UserNotFoundException(userId);

        assertThat(exception.getMessage()).contains("123");
        assertThat(exception.getMessage()).contains("Usuário não encontrado com id:");
        assertThat(exception.getErrorCode()).isEqualTo("USER_NOT_FOUND");
        assertThat(exception.getHttpStatusCode()).isEqualTo(404);
        assertThat(exception.getType()).isEqualTo(NOT_FOUND);
    }

    @Test
    @DisplayName("Should have correct error properties")
    void shouldHaveCorrectErrorProperties() {
        var exception = new UserNotFoundException(1);

        assertThat(exception).isInstanceOf(BusinessException.class).isInstanceOf(RuntimeException.class);
        assertThat(exception.getErrorCode()).isNotNull();
        assertThat(exception.getHttpStatusCode()).isPositive();
        assertThat(exception.getType()).isNotNull();
    }

    @Test
    @DisplayName("Should create exception with different user IDs")
    void shouldCreateException_WithDifferentUserIds() {
        var exception1 = new UserNotFoundException(1);
        var exception2 = new UserNotFoundException(999);

        assertThat(exception1.getMessage()).contains("1");
        assertThat(exception2.getMessage()).contains("999");
        assertThat(exception1.getErrorCode()).isEqualTo(exception2.getErrorCode());
        assertThat(exception1.getHttpStatusCode()).isEqualTo(exception2.getHttpStatusCode());
    }
}
