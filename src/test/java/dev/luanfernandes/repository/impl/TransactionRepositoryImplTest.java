package dev.luanfernandes.repository.impl;

import static dev.luanfernandes.domain.enums.TransactionType.EXPENSE;
import static dev.luanfernandes.domain.enums.TransactionType.INCOME;
import static org.assertj.core.api.Assertions.assertThat;

import dev.luanfernandes.domain.entity.Transaction;
import dev.luanfernandes.domain.entity.User;
import dev.luanfernandes.domain.enums.TransactionType;
import dev.luanfernandes.domain.enums.UserRole;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for TransactionRepositoryImpl")
class TransactionRepositoryImplTest {

    private TransactionRepositoryImpl transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository = new TransactionRepositoryImpl();
    }

    @Test
    @DisplayName("Should save new transaction and generate ID automatically")
    void shouldSaveNewTransaction_WhenIdIsNull() {
        var user = createUser(1);
        var transaction = createTransaction(INCOME, new BigDecimal("1000.00"), user);

        var savedTransaction = transactionRepository.save(transaction);

        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getId()).isNotNull();
        assertThat(savedTransaction.getId()).isEqualTo(1);
        assertThat(savedTransaction.getType()).isEqualTo(INCOME);
        assertThat(savedTransaction.getAmount()).isEqualTo(new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Should save multiple transactions with sequential IDs")
    void shouldSaveMultipleTransactions_WithSequentialIds() {
        var user = createUser(1);
        var transaction1 = createTransaction(INCOME, new BigDecimal("1000.00"), user);
        var transaction2 = createTransaction(EXPENSE, new BigDecimal("500.00"), user);
        var transaction3 = createTransaction(INCOME, new BigDecimal("2000.00"), user);

        var savedTransaction1 = transactionRepository.save(transaction1);
        var savedTransaction2 = transactionRepository.save(transaction2);
        var savedTransaction3 = transactionRepository.save(transaction3);

        assertThat(savedTransaction1.getId()).isEqualTo(1);
        assertThat(savedTransaction2.getId()).isEqualTo(2);
        assertThat(savedTransaction3.getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should update existing transaction when saving with existing ID")
    void shouldUpdateTransaction_WhenIdExists() {
        var user = createUser(1);
        var transaction = createTransaction(INCOME, new BigDecimal("1000.00"), user);
        var savedTransaction = transactionRepository.save(transaction);

        savedTransaction.setType(EXPENSE);
        savedTransaction.setAmount(new BigDecimal("1500.00"));
        var updatedTransaction = transactionRepository.save(savedTransaction);

        assertThat(updatedTransaction.getId()).isEqualTo(savedTransaction.getId());
        assertThat(updatedTransaction.getType()).isEqualTo(EXPENSE);
        assertThat(updatedTransaction.getAmount()).isEqualTo(new BigDecimal("1500.00"));

        var foundTransaction = transactionRepository.findById(savedTransaction.getId());
        assertThat(foundTransaction).isPresent();
        assertThat(foundTransaction.get().getAmount()).isEqualTo(new BigDecimal("1500.00"));
    }

    @Test
    @DisplayName("Should find all transactions successfully")
    void shouldFindAllTransactions_Successfully() {
        var user = createUser(1);
        var transaction1 = createTransaction(INCOME, new BigDecimal("1000.00"), user);
        var transaction2 = createTransaction(EXPENSE, new BigDecimal("500.00"), user);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        var transactions = transactionRepository.findAll();

        assertThat(transactions).hasSize(2);
        assertThat(transactions).extracting(Transaction::getType).containsExactlyInAnyOrder(INCOME, EXPENSE);
    }

    @Test
    @DisplayName("Should return empty list when no transactions exist")
    void shouldReturnEmptyList_WhenNoTransactions() {
        var transactions = transactionRepository.findAll();

        assertThat(transactions).isEmpty();
    }

    @Test
    @DisplayName("Should delete transaction by ID successfully")
    void shouldDeleteTransactionById_Successfully() {
        var user = createUser(1);
        var transaction = createTransaction(INCOME, new BigDecimal("1000.00"), user);
        var savedTransaction = transactionRepository.save(transaction);

        transactionRepository.deleteById(savedTransaction.getId());

        var foundTransaction = transactionRepository.findById(savedTransaction.getId());
        assertThat(foundTransaction).isEmpty();
    }

    @Test
    @DisplayName("Should find transaction by ID successfully")
    void shouldFindTransactionById_WhenTransactionExists() {
        var user = createUser(1);
        var transaction = createTransaction(INCOME, new BigDecimal("1000.00"), user);
        var savedTransaction = transactionRepository.save(transaction);

        var foundTransaction = transactionRepository.findById(savedTransaction.getId());

        assertThat(foundTransaction).isPresent();
        assertThat(foundTransaction.get().getId()).isEqualTo(savedTransaction.getId());
        assertThat(foundTransaction.get().getAmount()).isEqualTo(new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Should return empty when transaction not found by ID")
    void shouldReturnEmpty_WhenTransactionNotFound() {
        var foundTransaction = transactionRepository.findById(999);

        assertThat(foundTransaction).isEmpty();
    }

    @Test
    @DisplayName("Should find all transactions by user ID successfully")
    void shouldFindAllTransactionsByUserId_WhenUserHasTransactions() {
        var user1 = createUser(1);
        var user2 = createUser(2);

        var transaction1 = createTransaction(INCOME, new BigDecimal("1000.00"), user1);
        var transaction2 = createTransaction(EXPENSE, new BigDecimal("500.00"), user1);
        var transaction3 = createTransaction(INCOME, new BigDecimal("2000.00"), user2);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        var user1Transactions = transactionRepository.findAllByUserId(1);
        var user2Transactions = transactionRepository.findAllByUserId(2);

        assertThat(user1Transactions)
                .hasSize(2)
                .allMatch(t -> t.getUser().getId().equals(1));
        assertThat(user2Transactions)
                .hasSize(1)
                .allMatch(t -> t.getUser().getId().equals(2));
    }

    @Test
    @DisplayName("Should return empty list when user has no transactions")
    void shouldReturnEmptyList_WhenUserHasNoTransactions() {
        var user1 = createUser(1);
        var transaction = createTransaction(INCOME, new BigDecimal("1000.00"), user1);
        transactionRepository.save(transaction);

        var transactions = transactionRepository.findAllByUserId(999);

        assertThat(transactions).isEmpty();
    }

    @Test
    @DisplayName("Should return true when user has transactions")
    void shouldReturnTrue_WhenUserHasTransactions() {
        var user = createUser(1);
        var transaction = createTransaction(INCOME, new BigDecimal("1000.00"), user);
        transactionRepository.save(transaction);

        var exists = transactionRepository.existsByUserId(1);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when user has no transactions")
    void shouldReturnFalse_WhenUserHasNoTransactions() {
        var exists = transactionRepository.existsByUserId(999);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should handle transactions with null user correctly")
    void shouldHandleTransactionsWithNullUser_Correctly() {
        var user = createUser(1);
        var transaction1 = createTransaction(INCOME, new BigDecimal("1000.00"), user);
        var transaction2 = createTransaction(EXPENSE, new BigDecimal("500.00"), null);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        var userTransactions = transactionRepository.findAllByUserId(1);
        var exists = transactionRepository.existsByUserId(1);

        assertThat(userTransactions).hasSize(1);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when checking existence for user with only null user transactions")
    void shouldReturnFalse_WhenOnlyNullUserTransactionsExist() {
        var transaction1 = createTransaction(INCOME, new BigDecimal("1000.00"), null);
        var transaction2 = createTransaction(EXPENSE, new BigDecimal("500.00"), null);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        var exists = transactionRepository.existsByUserId(1);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return true when user has at least one transaction among null users")
    void shouldReturnTrue_WhenUserHasTransactionAmongNullUsers() {
        var user = createUser(5);
        var transaction1 = createTransaction(INCOME, new BigDecimal("1000.00"), null);
        var transaction2 = createTransaction(EXPENSE, new BigDecimal("500.00"), user);
        var transaction3 = createTransaction(INCOME, new BigDecimal("2000.00"), null);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        var exists = transactionRepository.existsByUserId(5);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should maintain data integrity after delete and save operations")
    void shouldMaintainDataIntegrity_AfterDeleteAndSave() {
        var user = createUser(1);
        var transaction1 = createTransaction(INCOME, new BigDecimal("1000.00"), user);
        var transaction2 = createTransaction(EXPENSE, new BigDecimal("500.00"), user);

        var savedTransaction1 = transactionRepository.save(transaction1);
        var savedTransaction2 = transactionRepository.save(transaction2);

        transactionRepository.deleteById(savedTransaction1.getId());

        var transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(1);
        assertThat(transactions.getFirst().getId()).isEqualTo(savedTransaction2.getId());

        var newTransaction = createTransaction(INCOME, new BigDecimal("2000.00"), user);
        var savedTransaction3 = transactionRepository.save(newTransaction);

        assertThat(savedTransaction3.getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should find transactions for specific user only")
    void shouldFindTransactionsForSpecificUserOnly() {
        var user1 = createUser(1);
        var user2 = createUser(2);
        var user3 = createUser(3);

        transactionRepository.save(createTransaction(INCOME, new BigDecimal("1000.00"), user1));
        transactionRepository.save(createTransaction(EXPENSE, new BigDecimal("500.00"), user1));
        transactionRepository.save(createTransaction(INCOME, new BigDecimal("2000.00"), user2));
        transactionRepository.save(createTransaction(EXPENSE, new BigDecimal("300.00"), user3));

        var user1Transactions = transactionRepository.findAllByUserId(1);
        var user2Transactions = transactionRepository.findAllByUserId(2);
        var user3Transactions = transactionRepository.findAllByUserId(3);

        assertThat(user1Transactions).hasSize(2);
        assertThat(user2Transactions).hasSize(1);
        assertThat(user3Transactions).hasSize(1);

        assertThat(user1Transactions)
                .extracting(Transaction::getAmount)
                .containsExactlyInAnyOrder(new BigDecimal("1000.00"), new BigDecimal("500.00"));
        assertThat(user2Transactions).extracting(Transaction::getAmount).containsExactly(new BigDecimal("2000.00"));
        assertThat(user3Transactions).extracting(Transaction::getAmount).containsExactly(new BigDecimal("300.00"));
    }

    @Test
    @DisplayName("Should exclude transactions when user ID does not match")
    void shouldExcludeTransactions_WhenUserIdDoesNotMatch() {
        var user1 = createUser(1);
        var user2 = createUser(2);

        var transaction1 = createTransaction(INCOME, new BigDecimal("1000.00"), user1);
        var transaction2 = createTransaction(EXPENSE, new BigDecimal("500.00"), user2);
        var transaction3 = createTransaction(INCOME, new BigDecimal("2000.00"), user1);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        var user1Transactions = transactionRepository.findAllByUserId(1);

        assertThat(user1Transactions)
                .hasSize(2)
                .allMatch(t -> t.getUser().getId().equals(1))
                .noneMatch(t -> t.getUser().getId().equals(2));
    }

    @Test
    @DisplayName("Should return false when checking existence for non-matching user ID")
    void shouldReturnFalse_WhenCheckingExistenceForNonMatchingUserId() {
        var user1 = createUser(1);

        var transaction1 = createTransaction(INCOME, new BigDecimal("1000.00"), user1);
        var transaction2 = createTransaction(EXPENSE, new BigDecimal("500.00"), user1);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        assertThat(transactionRepository.existsByUserId(1)).isTrue();
        assertThat(transactionRepository.existsByUserId(2)).isFalse();
        assertThat(transactionRepository.existsByUserId(999)).isFalse();
    }

    private User createUser(Integer id) {
        User user = new User();
        user.setId(id);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    private Transaction createTransaction(TransactionType type, BigDecimal amount, User user) {
        Transaction transaction = new Transaction();
        transaction.setId(null);
        transaction.setType(type);
        transaction.setCategory("Salário");
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDate.of(2025, 10, 10));
        transaction.setDescription("Test transaction");
        transaction.setUser(user);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        return transaction;
    }
}
