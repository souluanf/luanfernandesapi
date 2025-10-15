package dev.luanfernandes.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", 409),
    USER_NOT_FOUND("USER_NOT_FOUND", 404),
    VALIDATION_ERROR("VALIDATION_ERROR", 400),
    TRANSACTION_NOT_FOUND("TRANSACTION_NOT_FOUND", 404),
    USER_HAS_TRANSACTIONS("USER_HAS_TRANSACTIONS", 409);

    private final String code;
    private final int httpStatusCode;
}
