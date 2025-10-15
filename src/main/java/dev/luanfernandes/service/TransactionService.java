package dev.luanfernandes.service;

import dev.luanfernandes.dto.request.TransactionRequest;
import dev.luanfernandes.dto.response.MonthlyBalanceResponse;
import dev.luanfernandes.dto.response.TransactionResponse;
import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(TransactionRequest request, Integer userId);

    TransactionResponse getTransactionById(Integer id, Integer userId);

    List<TransactionResponse> listTransactions(Integer userId);

    TransactionResponse updateTransaction(Integer id, TransactionRequest request, Integer userId);

    void deleteTransaction(Integer id, Integer userId);

    MonthlyBalanceResponse getMonthlyBalance(int year, int month, Integer userId);
}
