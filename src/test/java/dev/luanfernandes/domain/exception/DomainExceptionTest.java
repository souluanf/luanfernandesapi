package dev.luanfernandes.domain.exception;

import static dev.luanfernandes.domain.enums.ErrorCode.TRANSACTION_NOT_FOUND;
import static dev.luanfernandes.domain.enums.ErrorCode.USER_ALREADY_EXISTS;
import static dev.luanfernandes.domain.enums.ErrorCode.USER_HAS_TRANSACTIONS;
import static dev.luanfernandes.domain.enums.ErrorCode.USER_NOT_FOUND;
import static dev.luanfernandes.domain.enums.ErrorCode.VALIDATION_ERROR;
import static dev.luanfernandes.domain.enums.ExceptionType.CONFLICT;
import static dev.luanfernandes.domain.enums.ExceptionType.VALIDATION;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for DomainException")
class DomainExceptionTest {

    @Test
    @DisplayName("Should create exception with error code, message and cause")
    void shouldCreateException_WithErrorCodeMessageAndCause() {
        var message = "Test message";
        var cause = new RuntimeException("Root cause");

        var exception = new DomainException(VALIDATION_ERROR, message, cause);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getErrorCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(exception.getHttpStatusCode()).isEqualTo(400);
        assertThat(exception.getType()).isEqualTo(VALIDATION);
    }

    @Test
    @DisplayName("Should return VALIDATION type for VALIDATION_ERROR")
    void shouldReturnValidationType_ForValidationError() {
        var exception = new DomainException(VALIDATION_ERROR, "Test", null);

        assertThat(exception.getType()).isEqualTo(VALIDATION);
        assertThat(exception.getHttpStatusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("Should return CONFLICT type for USER_ALREADY_EXISTS")
    void shouldReturnConflictType_ForUserAlreadyExists() {
        var exception = new DomainException(USER_ALREADY_EXISTS, "Test", null);

        assertThat(exception.getType()).isEqualTo(CONFLICT);
        assertThat(exception.getHttpStatusCode()).isEqualTo(409);
    }

    @Test
    @DisplayName("Should return CONFLICT type for USER_NOT_FOUND")
    void shouldReturnConflictType_ForUserNotFound() {
        var exception = new DomainException(USER_NOT_FOUND, "Test", null);

        assertThat(exception.getType()).isEqualTo(CONFLICT);
        assertThat(exception.getHttpStatusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("Should return CONFLICT type for TRANSACTION_NOT_FOUND")
    void shouldReturnConflictType_ForTransactionNotFound() {
        var exception = new DomainException(TRANSACTION_NOT_FOUND, "Test", null);

        assertThat(exception.getType()).isEqualTo(CONFLICT);
        assertThat(exception.getHttpStatusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("Should return CONFLICT type for USER_HAS_TRANSACTIONS")
    void shouldReturnConflictType_ForUserHasTransactions() {
        var exception = new DomainException(USER_HAS_TRANSACTIONS, "Test", null);

        assertThat(exception.getType()).isEqualTo(CONFLICT);
        assertThat(exception.getHttpStatusCode()).isEqualTo(409);
    }

    @Test
    @DisplayName("Should be instance of BusinessException")
    void shouldBeInstanceOfBusinessException() {
        var exception = new DomainException(VALIDATION_ERROR, "Test", null);

        assertThat(exception).isInstanceOf(BusinessException.class).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should store errorCode correctly")
    void shouldStoreErrorCodeCorrectly() {
        var exception = new DomainException(USER_NOT_FOUND, "Test", null);

        assertThat(exception.getErrorCode()).isEqualTo("USER_NOT_FOUND");
        assertThat(exception.getErrorCode()).isEqualTo(USER_NOT_FOUND.getCode());
    }
}
