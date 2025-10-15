package dev.luanfernandes.controller.impl;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import dev.luanfernandes.controller.TransactionController;
import dev.luanfernandes.dto.request.TransactionRequest;
import dev.luanfernandes.dto.response.MonthlyBalanceResponse;
import dev.luanfernandes.dto.response.TransactionResponse;
import dev.luanfernandes.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;

    @Override
    public ResponseEntity<TransactionResponse> createTransaction(TransactionRequest request, Integer userId) {
        return status(CREATED).body(transactionService.createTransaction(request, userId));
    }

    @Override
    public ResponseEntity<TransactionResponse> getTransactionById(Integer id, Integer userId) {
        return ok(transactionService.getTransactionById(id, userId));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> listTransactions(Integer userId) {
        return ok(transactionService.listTransactions(userId));
    }

    @Override
    public ResponseEntity<TransactionResponse> updateTransaction(
            Integer id, TransactionRequest request, Integer userId) {
        return ok(transactionService.updateTransaction(id, request, userId));
    }

    @Override
    public ResponseEntity<Void> deleteTransaction(Integer id, Integer userId) {
        transactionService.deleteTransaction(id, userId);
        return noContent().build();
    }

    @Override
    public ResponseEntity<MonthlyBalanceResponse> getMonthlyBalance(int year, int month, Integer userId) {
        return ok(transactionService.getMonthlyBalance(year, month, userId));
    }
}
