package dev.luanfernandes.domain.exception;

import static dev.luanfernandes.domain.enums.ExceptionType.CONFLICT;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for UserAlreadyExistsException")
class UserAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Should create exception with email message")
    void shouldCreateException_WithEmailMessage() {
        var email = "test@example.com";

        var exception = new UserAlreadyExistsException(email);

        assertThat(exception.getMessage()).contains(email);
        assertThat(exception.getMessage()).contains("Usuário já existe com o email:");
        assertThat(exception.getErrorCode()).isEqualTo("USER_ALREADY_EXISTS");
        assertThat(exception.getHttpStatusCode()).isEqualTo(409);
        assertThat(exception.getType()).isEqualTo(CONFLICT);
    }

    @Test
    @DisplayName("Should have correct error properties")
    void shouldHaveCorrectErrorProperties() {
        var exception = new UserAlreadyExistsException("user@test.com");

        assertThat(exception).isInstanceOf(BusinessException.class).isInstanceOf(RuntimeException.class);
        assertThat(exception.getErrorCode()).isNotNull();
        assertThat(exception.getHttpStatusCode()).isPositive();
        assertThat(exception.getType()).isNotNull();
    }

    @Test
    @DisplayName("Should create exception with different emails")
    void shouldCreateException_WithDifferentEmails() {
        var exception1 = new UserAlreadyExistsException("user1@example.com");
        var exception2 = new UserAlreadyExistsException("user2@example.com");

        assertThat(exception1.getMessage()).contains("user1@example.com");
        assertThat(exception2.getMessage()).contains("user2@example.com");
        assertThat(exception1.getErrorCode()).isEqualTo(exception2.getErrorCode());
        assertThat(exception1.getHttpStatusCode()).isEqualTo(exception2.getHttpStatusCode());
    }

    @Test
    @DisplayName("Should have HTTP status 409 Conflict")
    void shouldHaveHttpStatus409Conflict() {
        var exception = new UserAlreadyExistsException("test@example.com");

        assertThat(exception.getHttpStatusCode()).isEqualTo(409);
        assertThat(exception.getType()).isEqualTo(CONFLICT);
    }
}
