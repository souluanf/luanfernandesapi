package dev.luanfernandes.controller.impl;

import static dev.luanfernandes.domain.enums.TransactionType.EXPENSE;
import static dev.luanfernandes.domain.enums.TransactionType.INCOME;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import dev.luanfernandes.dto.request.TransactionRequest;
import dev.luanfernandes.dto.response.MonthlyBalanceResponse;
import dev.luanfernandes.dto.response.TransactionResponse;
import dev.luanfernandes.service.TransactionService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for TransactionControllerImpl")
class TransactionControllerImplTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionControllerImpl transactionController;

    @Test
    @DisplayName("Should create transaction successfully when valid data provided")
    void shouldCreateTransaction_WhenValidData() {
        var userId = 1;
        var request = createTransactionRequest();
        var expectedResponse = createTransactionResponse();

        when(transactionService.createTransaction(any(TransactionRequest.class), eq(userId)))
                .thenReturn(expectedResponse);

        var result = transactionController.createTransaction(request, userId);

        assertThat(result.getStatusCode()).isEqualTo(CREATED);
        assertThat(result.getBody()).isEqualTo(expectedResponse);
        verify(transactionService).createTransaction(request, userId);
    }

    @Test
    @DisplayName("Should get transaction by ID successfully")
    void shouldGetTransactionById_WhenValidId() {
        var userId = 1;
        var transactionId = 1;
        var expectedResponse = createTransactionResponse();

        when(transactionService.getTransactionById(transactionId, userId)).thenReturn(expectedResponse);

        var result = transactionController.getTransactionById(transactionId, userId);

        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isEqualTo(expectedResponse);
        verify(transactionService).getTransactionById(transactionId, userId);
    }

    @Test
    @DisplayName("Should list transactions successfully for user")
    void shouldListTransactions_Successfully() {
        var userId = 1;
        var transaction = createTransactionResponse();
        var expectedList = List.of(transaction);

        when(transactionService.listTransactions(userId)).thenReturn(expectedList);

        var result = transactionController.listTransactions(userId);

        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody()).isEqualTo(expectedList);
        verify(transactionService).listTransactions(userId);
    }

    @Test
    @DisplayName("Should return empty list when user has no transactions")
    void shouldReturnEmptyList_WhenNoTransactions() {
        var userId = 1;

        when(transactionService.listTransactions(userId)).thenReturn(List.of());

        var result = transactionController.listTransactions(userId);

        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody()).isEmpty();
        verify(transactionService).listTransactions(userId);
    }

    @Test
    @DisplayName("Should update transaction successfully")
    void shouldUpdateTransaction_WhenValidData() {
        var userId = 1;
        var transactionId = 1;
        var request = createTransactionRequest();
        var expectedResponse = createTransactionResponse();

        when(transactionService.updateTransaction(transactionId, request, userId))
                .thenReturn(expectedResponse);

        var result = transactionController.updateTransaction(transactionId, request, userId);

        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isEqualTo(expectedResponse);
        verify(transactionService).updateTransaction(transactionId, request, userId);
    }

    @Test
    @DisplayName("Should update transaction with different transaction type")
    void shouldUpdateTransaction_WhenChangingType() {
        var userId = 1;
        var transactionId = 1;
        var request = TransactionRequest.builder()
                .type(EXPENSE)
                .category("Alimentação")
                .amount(new BigDecimal("150.00"))
                .transactionDate(LocalDate.of(2025, 10, 10))
                .description("Almoço")
                .build();

        var expectedResponse = new TransactionResponse(
                1,
                EXPENSE,
                "Alimentação",
                new BigDecimal("150.00"),
                LocalDate.of(2025, 10, 10),
                "Almoço",
                "1",
                LocalDateTime.now(),
                LocalDateTime.now());

        when(transactionService.updateTransaction(transactionId, request, userId))
                .thenReturn(expectedResponse);

        var result = transactionController.updateTransaction(transactionId, request, userId);

        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().type()).isEqualTo(EXPENSE);
        verify(transactionService).updateTransaction(transactionId, request, userId);
    }

    @Test
    @DisplayName("Should delete transaction successfully")
    void shouldDeleteTransaction_WhenValidId() {
        var userId = 1;
        var transactionId = 1;

        doNothing().when(transactionService).deleteTransaction(transactionId, userId);

        var result = transactionController.deleteTransaction(transactionId, userId);

        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(result.getBody()).isNull();
        verify(transactionService).deleteTransaction(transactionId, userId);
    }

    @Test
    @DisplayName("Should get monthly balance successfully")
    void shouldGetMonthlyBalance_Successfully() {
        var userId = 1;
        var year = 2025;
        var month = 10;
        var expectedResponse = MonthlyBalanceResponse.builder()
                .year(year)
                .month(month)
                .totalIncome(new BigDecimal("6500.00"))
                .totalExpense(new BigDecimal("2800.00"))
                .balance(new BigDecimal("3700.00"))
                .build();

        when(transactionService.getMonthlyBalance(year, month, userId)).thenReturn(expectedResponse);

        var result = transactionController.getMonthlyBalance(year, month, userId);

        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().year()).isEqualTo(year);
        assertThat(result.getBody().month()).isEqualTo(month);
        assertThat(result.getBody().totalIncome()).isEqualTo(new BigDecimal("6500.00"));
        assertThat(result.getBody().totalExpense()).isEqualTo(new BigDecimal("2800.00"));
        assertThat(result.getBody().balance()).isEqualTo(new BigDecimal("3700.00"));
        verify(transactionService).getMonthlyBalance(year, month, userId);
    }

    @Test
    @DisplayName("Should return zero balance when no transactions in month")
    void shouldReturnZeroBalance_WhenNoTransactionsInMonth() {
        var userId = 1;
        var year = 2025;
        var month = 11;
        var expectedResponse = MonthlyBalanceResponse.builder()
                .year(year)
                .month(month)
                .totalIncome(ZERO)
                .totalExpense(ZERO)
                .balance(ZERO)
                .build();

        when(transactionService.getMonthlyBalance(year, month, userId)).thenReturn(expectedResponse);

        var result = transactionController.getMonthlyBalance(year, month, userId);

        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().totalIncome()).isEqualTo(ZERO);
        assertThat(result.getBody().totalExpense()).isEqualTo(ZERO);
        assertThat(result.getBody().balance()).isEqualTo(ZERO);
        verify(transactionService).getMonthlyBalance(year, month, userId);
    }

    @Test
    @DisplayName("Should create expense transaction successfully")
    void shouldCreateExpenseTransaction_Successfully() {
        var userId = 1;
        var request = TransactionRequest.builder()
                .type(EXPENSE)
                .category("Transporte")
                .amount(new BigDecimal("80.00"))
                .transactionDate(LocalDate.of(2025, 10, 10))
                .description("Uber")
                .build();

        var expectedResponse = new TransactionResponse(
                1,
                EXPENSE,
                "Transporte",
                new BigDecimal("80.00"),
                LocalDate.of(2025, 10, 10),
                "Uber",
                "1",
                LocalDateTime.now(),
                LocalDateTime.now());

        when(transactionService.createTransaction(any(TransactionRequest.class), eq(userId)))
                .thenReturn(expectedResponse);

        var result = transactionController.createTransaction(request, userId);

        assertThat(result.getStatusCode()).isEqualTo(CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().type()).isEqualTo(EXPENSE);
        assertThat(result.getBody().category()).isEqualTo("Transporte");
        verify(transactionService).createTransaction(request, userId);
    }

    private TransactionRequest createTransactionRequest() {
        return TransactionRequest.builder()
                .type(INCOME)
                .category("Salário")
                .amount(new BigDecimal("5000.00"))
                .transactionDate(LocalDate.of(2025, 10, 10))
                .description("Salário mensal")
                .build();
    }

    private TransactionResponse createTransactionResponse() {
        return new TransactionResponse(
                1,
                INCOME,
                "Salário",
                new BigDecimal("5000.00"),
                LocalDate.of(2025, 10, 10),
                "Salário mensal",
                "1",
                LocalDateTime.now(),
                LocalDateTime.now());
    }
}
