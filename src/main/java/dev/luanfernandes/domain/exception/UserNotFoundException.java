package dev.luanfernandes.domain.exception;

import dev.luanfernandes.domain.enums.ErrorCode;
import dev.luanfernandes.domain.enums.ExceptionType;
import java.io.Serial;

public class UserNotFoundException extends BusinessException {

    @Serial
    private static final long serialVersionUID = -8472956724834578234L;

    public UserNotFoundException(Integer id) {
        super("Usuário não encontrado com id: " + id);
    }

    private static final ErrorCode ERROR_CODE = ErrorCode.USER_NOT_FOUND;

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
        return ExceptionType.NOT_FOUND;
    }
}
