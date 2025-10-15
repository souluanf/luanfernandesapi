package dev.luanfernandes.service.impl;

import static dev.luanfernandes.domain.enums.TransactionType.EXPENSE;
import static dev.luanfernandes.domain.enums.TransactionType.INCOME;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.domain.entity.Transaction;
import dev.luanfernandes.domain.entity.User;
import dev.luanfernandes.domain.enums.UserRole;
import dev.luanfernandes.domain.exception.TransactionAccessDeniedException;
import dev.luanfernandes.domain.exception.TransactionNotFoundException;
import dev.luanfernandes.domain.exception.UserNotFoundException;
import dev.luanfernandes.dto.mapper.TransactionMapper;
import dev.luanfernandes.dto.request.TransactionRequest;
import dev.luanfernandes.dto.response.TransactionResponse;
import dev.luanfernandes.repository.TransactionRepository;
import dev.luanfernandes.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for TransactionServiceImpl")
class TransactionServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Should create transaction successfully when valid data provided")
    void shouldCreateTransaction_WhenValidData() {
        var userId = 1;
        var request = createTransactionRequest();
        var user = createTestUser();
        var transaction = createTestTransaction();
        var savedTransaction = createTestTransaction();
        var expectedResponse = createTransactionResponse();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionMapper.toEntity(request)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        when(transactionMapper.toResponse(savedTransaction)).thenReturn(expectedResponse);

        var result = transactionService.createTransaction(request, userId);

        assertThat(result).isEqualTo(expectedResponse);
        verify(userRepository).findById(userId);
        verify(transactionMapper).toEntity(request);
        verify(transactionRepository).save(transaction);
        verify(transactionMapper).toResponse(savedTransaction);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when creating transaction for non-existent user")
    void shouldThrowException_WhenCreatingTransactionForNonExistentUser() {
        var userId = 999;
        var request = createTransactionRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createTransaction(request, userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should get transaction by ID successfully when transaction belongs to user")
    void shouldGetTransactionById_WhenTransactionBelongsToUser() {
        var userId = 1;
        var transactionId = 1;
        var user = createTestUser();
        var transaction = createTestTransaction();
        transaction.setUser(user);
        var expectedResponse = createTransactionResponse();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toResponse(transaction)).thenReturn(expectedResponse);

        var result = transactionService.getTransactionById(transactionId, userId);

        assertThat(result).isEqualTo(expectedResponse);
        verify(userRepository).findById(userId);
        verify(transactionRepository).findById(transactionId);
        verify(transactionMapper).toResponse(transaction);
    }

    @Test
    @DisplayName("Should throw TransactionNotFoundException when transaction not found")
    void shouldThrowException_WhenTransactionNotFound() {
        var userId = 1;
        var transactionId = 999;
        var user = createTestUser();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getTransactionById(transactionId, userId))
                .isInstanceOf(TransactionNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Should throw TransactionAccessDeniedException when transaction belongs to another user")
    void shouldThrowException_WhenTransactionBelongsToAnotherUser() {
        var userId = 1;
        var transactionId = 1;
        var user = createTestUser();
        var otherUser = createTestUser();
        otherUser.setId(2);
        var transaction = createTestTransaction();
        transaction.setUser(otherUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        assertThatThrownBy(() -> transactionService.getTransactionById(transactionId, userId))
                .isInstanceOf(TransactionAccessDeniedException.class);

        verify(userRepository).findById(userId);
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Should throw TransactionAccessDeniedException when transaction has null user")
    void shouldThrowException_WhenTransactionHasNullUser() {
        var userId = 1;
        var transactionId = 1;
        var user = createTestUser();
        var transaction = createTestTransaction();
        transaction.setUser(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        assertThatThrownBy(() -> transactionService.getTransactionById(transactionId, userId))
                .isInstanceOf(TransactionAccessDeniedException.class);

        verify(userRepository).findById(userId);
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Should list transactions successfully for user")
    void shouldListTransactions_Successfully() {
        var userId = 1;
        var user = createTestUser();
        var transactions = List.of(createTestTransaction());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findAllByUserId(userId)).thenReturn(transactions);
        when(transactionMapper.toResponse(any(Transaction.class))).thenReturn(createTransactionResponse());

        var result = transactionService.listTransactions(userId);

        assertThat(result).hasSize(1);
        verify(userRepository).findById(userId);
        verify(transactionRepository).findAllByUserId(userId);
    }

    @Test
    @DisplayName("Should update transaction successfully when transaction belongs to user")
    void shouldUpdateTransaction_WhenTransactionBelongsToUser() {
        var userId = 1;
        var transactionId = 1;
        var request = createTransactionRequest();
        var user = createTestUser();
        var transaction = createTestTransaction();
        transaction.setUser(user);
        var updatedTransaction = createTestTransaction();
        var expectedResponse = createTransactionResponse();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(updatedTransaction);
        when(transactionMapper.toResponse(updatedTransaction)).thenReturn(expectedResponse);

        var result = transactionService.updateTransaction(transactionId, request, userId);

        assertThat(result).isEqualTo(expectedResponse);
        verify(userRepository).findById(userId);
        verify(transactionRepository).findById(transactionId);
        verify(transactionMapper).updateEntityFromRequest(request, transaction);
        verify(transactionRepository).save(transaction);
        verify(transactionMapper).toResponse(updatedTransaction);
    }

    @Test
    @DisplayName("Should throw TransactionAccessDeniedException when updating transaction of another user")
    void shouldThrowException_WhenUpdatingTransactionOfAnotherUser() {
        var userId = 1;
        var transactionId = 1;
        var request = createTransactionRequest();
        var user = createTestUser();
        var otherUser = createTestUser();
        otherUser.setId(2);
        var transaction = createTestTransaction();
        transaction.setUser(otherUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        assertThatThrownBy(() -> transactionService.updateTransaction(transactionId, request, userId))
                .isInstanceOf(TransactionAccessDeniedException.class);

        verify(userRepository).findById(userId);
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Should throw TransactionAccessDeniedException when updating transaction with null user")
    void shouldThrowException_WhenUpdatingTransactionWithNullUser() {
        var userId = 1;
        var transactionId = 1;
        var request = createTransactionRequest();
        var user = createTestUser();
        var transaction = createTestTransaction();
        transaction.setUser(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        assertThatThrownBy(() -> transactionService.updateTransaction(transactionId, request, userId))
                .isInstanceOf(TransactionAccessDeniedException.class);

        verify(userRepository).findById(userId);
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Should delete transaction successfully when transaction belongs to user")
    void shouldDeleteTransaction_WhenTransactionBelongsToUser() {
        var userId = 1;
        var transactionId = 1;
        var user = createTestUser();
        var transaction = createTestTransaction();
        transaction.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(transactionId, userId);

        verify(userRepository).findById(userId);
        verify(transactionRepository).findById(transactionId);
        verify(transactionRepository).deleteById(transactionId);
    }

    @Test
    @DisplayName("Should throw TransactionAccessDeniedException when deleting transaction of another user")
    void shouldThrowException_WhenDeletingTransactionOfAnotherUser() {
        var userId = 1;
        var transactionId = 1;
        var user = createTestUser();
        var otherUser = createTestUser();
        otherUser.setId(2);
        var transaction = createTestTransaction();
        transaction.setUser(otherUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        assertThatThrownBy(() -> transactionService.deleteTransaction(transactionId, userId))
                .isInstanceOf(TransactionAccessDeniedException.class);

        verify(userRepository).findById(userId);
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Should throw TransactionAccessDeniedException when deleting transaction with null user")
    void shouldThrowException_WhenDeletingTransactionWithNullUser() {
        var userId = 1;
        var transactionId = 1;
        var user = createTestUser();
        var transaction = createTestTransaction();
        transaction.setUser(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        assertThatThrownBy(() -> transactionService.deleteTransaction(transactionId, userId))
                .isInstanceOf(TransactionAccessDeniedException.class);

        verify(userRepository).findById(userId);
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Should calculate monthly balance successfully")
    void shouldCalculateMonthlyBalance_Successfully() {
        var userId = 1;
        var year = 2025;
        var month = 10;
        var user = createTestUser();

        var income1 = createTestTransaction();
        income1.setType(INCOME);
        income1.setAmount(new BigDecimal("5000.00"));
        income1.setTransactionDate(LocalDate.of(2025, 10, 5));

        var income2 = createTestTransaction();
        income2.setType(INCOME);
        income2.setAmount(new BigDecimal("1500.00"));
        income2.setTransactionDate(LocalDate.of(2025, 10, 15));

        var expense1 = createTestTransaction();
        expense1.setType(EXPENSE);
        expense1.setAmount(new BigDecimal("2000.00"));
        expense1.setTransactionDate(LocalDate.of(2025, 10, 10));

        var expense2 = createTestTransaction();
        expense2.setType(EXPENSE);
        expense2.setAmount(new BigDecimal("800.00"));
        expense2.setTransactionDate(LocalDate.of(2025, 10, 20));

        var transactions = List.of(income1, income2, expense1, expense2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findAllByUserId(userId)).thenReturn(transactions);

        var result = transactionService.getMonthlyBalance(year, month, userId);

        assertThat(result.year()).isEqualTo(year);
        assertThat(result.month()).isEqualTo(month);
        assertThat(result.totalIncome()).isEqualTo(new BigDecimal("6500.00"));
        assertThat(result.totalExpense()).isEqualTo(new BigDecimal("2800.00"));
        assertThat(result.balance()).isEqualTo(new BigDecimal("3700.00"));

        verify(userRepository).findById(userId);
        verify(transactionRepository).findAllByUserId(userId);
    }

    @Test
    @DisplayName("Should return zero balance when no transactions in month")
    void shouldReturnZeroBalance_WhenNoTransactionsInMonth() {
        var userId = 1;
        var year = 2025;
        var month = 10;
        var user = createTestUser();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findAllByUserId(userId)).thenReturn(List.of());

        var result = transactionService.getMonthlyBalance(year, month, userId);

        assertThat(result.totalIncome()).isEqualTo(ZERO);
        assertThat(result.totalExpense()).isEqualTo(ZERO);
        assertThat(result.balance()).isEqualTo(ZERO);

        verify(userRepository).findById(userId);
        verify(transactionRepository).findAllByUserId(userId);
    }

    @Test
    @DisplayName("Should exclude transactions before start date from monthly balance")
    void shouldExcludeTransactionsBeforeStartDate_FromMonthlyBalance() {
        var userId = 1;
        var year = 2025;
        var month = 10;
        var user = createTestUser();

        var incomeInRange = createTestTransaction();
        incomeInRange.setType(INCOME);
        incomeInRange.setAmount(new BigDecimal("3000.00"));
        incomeInRange.setTransactionDate(LocalDate.of(2025, 10, 5));

        var incomeBeforeRange = createTestTransaction();
        incomeBeforeRange.setType(INCOME);
        incomeBeforeRange.setAmount(new BigDecimal("5000.00"));
        incomeBeforeRange.setTransactionDate(LocalDate.of(2025, 9, 30));

        var transactions = List.of(incomeInRange, incomeBeforeRange);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findAllByUserId(userId)).thenReturn(transactions);

        var result = transactionService.getMonthlyBalance(year, month, userId);

        assertThat(result.totalIncome()).isEqualTo(new BigDecimal("3000.00"));
        assertThat(result.totalExpense()).isEqualTo(ZERO);

        verify(userRepository).findById(userId);
        verify(transactionRepository).findAllByUserId(userId);
    }

    @Test
    @DisplayName("Should exclude transactions after end date from monthly balance")
    void shouldExcludeTransactionsAfterEndDate_FromMonthlyBalance() {
        var userId = 1;
        var year = 2025;
        var month = 10;
        var user = createTestUser();

        var expenseInRange = createTestTransaction();
        expenseInRange.setType(EXPENSE);
        expenseInRange.setAmount(new BigDecimal("1500.00"));
        expenseInRange.setTransactionDate(LocalDate.of(2025, 10, 20));

        var expenseAfterRange = createTestTransaction();
        expenseAfterRange.setType(EXPENSE);
        expenseAfterRange.setAmount(new BigDecimal("2500.00"));
        expenseAfterRange.setTransactionDate(LocalDate.of(2025, 11, 1));

        var transactions = List.of(expenseInRange, expenseAfterRange);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findAllByUserId(userId)).thenReturn(transactions);

        var result = transactionService.getMonthlyBalance(year, month, userId);

        assertThat(result.totalIncome()).isEqualTo(ZERO);
        assertThat(result.totalExpense()).isEqualTo(new BigDecimal("1500.00"));

        verify(userRepository).findById(userId);
        verify(transactionRepository).findAllByUserId(userId);
    }

    @Test
    @DisplayName("Should include transactions on start date in monthly balance")
    void shouldIncludeTransactionsOnStartDate_InMonthlyBalance() {
        var userId = 1;
        var year = 2025;
        var month = 10;
        var user = createTestUser();

        var incomeOnStartDate = createTestTransaction();
        incomeOnStartDate.setType(INCOME);
        incomeOnStartDate.setAmount(new BigDecimal("2000.00"));
        incomeOnStartDate.setTransactionDate(LocalDate.of(2025, 10, 1));

        var transactions = List.of(incomeOnStartDate);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findAllByUserId(userId)).thenReturn(transactions);

        var result = transactionService.getMonthlyBalance(year, month, userId);

        assertThat(result.totalIncome()).isEqualTo(new BigDecimal("2000.00"));

        verify(userRepository).findById(userId);
        verify(transactionRepository).findAllByUserId(userId);
    }

    @Test
    @DisplayName("Should include transactions on end date in monthly balance")
    void shouldIncludeTransactionsOnEndDate_InMonthlyBalance() {
        var userId = 1;
        var year = 2025;
        var month = 10;
        var user = createTestUser();

        var expenseOnEndDate = createTestTransaction();
        expenseOnEndDate.setType(EXPENSE);
        expenseOnEndDate.setAmount(new BigDecimal("1200.00"));
        expenseOnEndDate.setTransactionDate(LocalDate.of(2025, 10, 31));

        var transactions = List.of(expenseOnEndDate);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findAllByUserId(userId)).thenReturn(transactions);

        var result = transactionService.getMonthlyBalance(year, month, userId);

        assertThat(result.totalExpense()).isEqualTo(new BigDecimal("1200.00"));

        verify(userRepository).findById(userId);
        verify(transactionRepository).findAllByUserId(userId);
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    private Transaction createTestTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setType(INCOME);
        transaction.setCategory("Salário");
        transaction.setAmount(new BigDecimal("5000.00"));
        transaction.setTransactionDate(LocalDate.of(2025, 10, 10));
        transaction.setDescription("Test transaction");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        return transaction;
    }

    private TransactionRequest createTransactionRequest() {
        return TransactionRequest.builder()
                .type(INCOME)
                .category("Salário")
                .amount(new BigDecimal("5000.00"))
                .transactionDate(LocalDate.of(2025, 10, 10))
                .description("Test transaction")
                .build();
    }

    private TransactionResponse createTransactionResponse() {
        return new TransactionResponse(
                1,
                INCOME,
                "Salário",
                new BigDecimal("5000.00"),
                LocalDate.of(2025, 10, 10),
                "Test transaction",
                "1",
                LocalDateTime.now(),
                LocalDateTime.now());
    }
}
