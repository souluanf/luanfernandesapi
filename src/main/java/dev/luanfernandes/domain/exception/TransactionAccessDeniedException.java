package dev.luanfernandes.domain.exception;

import dev.luanfernandes.domain.enums.ErrorCode;
import dev.luanfernandes.domain.enums.ExceptionType;
import java.io.Serial;

public class TransactionAccessDeniedException extends BusinessException {

    @Serial
    private static final long serialVersionUID = -8472956724834578236L;

    private static final ErrorCode ERROR_CODE = ErrorCode.TRANSACTION_NOT_FOUND;

    public TransactionAccessDeniedException(Integer transactionId) {
        super("Acesso negado à transação com id " + transactionId);
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
        return ExceptionType.NOT_FOUND;
    }
}
