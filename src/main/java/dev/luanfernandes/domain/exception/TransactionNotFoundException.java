package dev.luanfernandes.domain.exception;

import dev.luanfernandes.domain.enums.ErrorCode;
import dev.luanfernandes.domain.enums.ExceptionType;
import java.io.Serial;

public class TransactionNotFoundException extends BusinessException {

    @Serial
    private static final long serialVersionUID = -8472956724834578234L;

    public TransactionNotFoundException(Integer id) {
        super("Transação não encontrada com id: " + id);
    }

    private static final ErrorCode ERROR_CODE = ErrorCode.TRANSACTION_NOT_FOUND;

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
