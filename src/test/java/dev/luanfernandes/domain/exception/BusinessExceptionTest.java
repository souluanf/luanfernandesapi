package dev.luanfernandes.domain.exception;

import static dev.luanfernandes.domain.enums.ExceptionType.VALIDATION;
import static org.assertj.core.api.Assertions.assertThat;

import dev.luanfernandes.domain.enums.ExceptionType;
import java.io.Serial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for BusinessException")
class BusinessExceptionTest {

    @Test
    @DisplayName("Should create exception with message only")
    void shouldCreateException_WithMessageOnly() {
        var message = "Test error message";

        var exception = new TestBusinessException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateException_WithMessageAndCause() {
        var message = "Test error message";
        var cause = new RuntimeException("Root cause");

        var exception = new TestBusinessException(message, cause);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should be instance of RuntimeException")
    void shouldBeInstanceOfRuntimeException() {
        var exception = new TestBusinessException("Test");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should have default HTTP status code 400")
    void shouldHaveDefaultHttpStatusCode400() {
        var exception = new TestBusinessException("Test");

        assertThat(exception.getHttpStatusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("Should call abstract methods correctly")
    void shouldCallAbstractMethodsCorrectly() {
        var exception = new TestBusinessException("Test");

        assertThat(exception.getErrorCode()).isEqualTo("TEST_ERROR");
        assertThat(exception.getType()).isEqualTo(VALIDATION);
        assertThat(exception.getHttpStatusCode()).isEqualTo(400);
    }

    private static class TestBusinessException extends BusinessException {

        @Serial
        private static final long serialVersionUID = 8947091864280602213L;

        public TestBusinessException(String message) {
            super(message);
        }

        public TestBusinessException(String message, Throwable cause) {
            super(message, cause);
        }

        @Override
        public String getErrorCode() {
            return "TEST_ERROR";
        }

        @Override
        public ExceptionType getType() {
            return VALIDATION;
        }
    }
}
