package dev.luanfernandes.domain.exception;

import dev.luanfernandes.domain.enums.ErrorCode;
import dev.luanfernandes.domain.enums.ExceptionType;
import java.io.Serial;

public class UserHasTransactionsException extends BusinessException {

    private static final ErrorCode ERROR_CODE = ErrorCode.USER_HAS_TRANSACTIONS;

    @Serial
    private static final long serialVersionUID = -8472956724834578235L;

    public UserHasTransactionsException(Integer userId) {
        super("Não é possível deletar o usuário com id " + userId + " pois existem transações associadas");
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
