package dev.luanfernandes.domain.exception;

import dev.luanfernandes.domain.enums.ErrorCode;
import dev.luanfernandes.domain.enums.ExceptionType;
import java.io.Serial;

public class UserAlreadyExistsException extends BusinessException {

    private static final ErrorCode ERROR_CODE = ErrorCode.USER_ALREADY_EXISTS;

    @Serial
    private static final long serialVersionUID = -4227606701914326086L;

    public UserAlreadyExistsException(String email) {
        super("Usuário já existe com o email: " + email);
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE.getCode();
    }

    @Override
    public int getHttpStatusCode() {
        return ERROR_CODE.getHttpStatusCode();
    }

    @Override
    public ExceptionType getType() {
        return ExceptionType.CONFLICT;
    }
}
