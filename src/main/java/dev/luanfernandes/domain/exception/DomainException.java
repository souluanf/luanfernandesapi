package dev.luanfernandes.domain.exception;

import dev.luanfernandes.domain.enums.ErrorCode;
import dev.luanfernandes.domain.enums.ExceptionType;
import java.io.Serial;
import lombok.Getter;

@Getter
public class DomainException extends BusinessException {

    @Serial
    private static final long serialVersionUID = -8472967899243578627L;

    private final ErrorCode errorCode;

    public DomainException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode.getCode();
    }

    @Override
    public int getHttpStatusCode() {
        return errorCode.getHttpStatusCode();
    }

    @Override
    public ExceptionType getType() {
        return switch (errorCode) {
            case USER_ALREADY_EXISTS, USER_NOT_FOUND, TRANSACTION_NOT_FOUND, USER_HAS_TRANSACTIONS ->
                ExceptionType.CONFLICT;
            case VALIDATION_ERROR -> ExceptionType.VALIDATION;
        };
    }
}
