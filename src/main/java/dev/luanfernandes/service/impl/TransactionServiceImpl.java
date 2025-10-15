package dev.luanfernandes.service.impl;

import static dev.luanfernandes.domain.enums.TransactionType.EXPENSE;
import static dev.luanfernandes.domain.enums.TransactionType.INCOME;
import static java.math.BigDecimal.ZERO;

import dev.luanfernandes.domain.entity.Transaction;
import dev.luanfernandes.domain.entity.User;
import dev.luanfernandes.domain.enums.TransactionType;
import dev.luanfernandes.domain.exception.TransactionAccessDeniedException;
import dev.luanfernandes.domain.exception.TransactionNotFoundException;
import dev.luanfernandes.domain.exception.UserNotFoundException;
import dev.luanfernandes.dto.mapper.TransactionMapper;
import dev.luanfernandes.dto.request.TransactionRequest;
import dev.luanfernandes.dto.response.MonthlyBalanceResponse;
import dev.luanfernandes.dto.response.TransactionResponse;
import dev.luanfernandes.repository.TransactionRepository;
import dev.luanfernandes.repository.UserRepository;
import dev.luanfernandes.service.TransactionService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionResponse createTransaction(TransactionRequest request, Integer userId) {
        User user = findUserById(userId);

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setUser(user);

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info(
                "Transação criada: tipo={}, valor={}, usuário={}",
                savedTransaction.getType(),
                savedTransaction.getAmount(),
                userId);
        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    public TransactionResponse getTransactionById(Integer id, Integer userId) {
        findUserById(userId);
        Transaction transaction =
                transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        validateTransactionOwnership(transaction, userId);
        return transactionMapper.toResponse(transaction);
    }

    @Override
    public List<TransactionResponse> listTransactions(Integer userId) {
        findUserById(userId);
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);
        return transactions.stream().map(transactionMapper::toResponse).toList();
    }

    @Override
    public TransactionResponse updateTransaction(Integer id, TransactionRequest request, Integer userId) {
        findUserById(userId);

        Transaction transaction =
                transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        validateTransactionOwnership(transaction, userId);

        transactionMapper.updateEntityFromRequest(request, transaction);
        Transaction updatedTransaction = transactionRepository.save(transaction);

        log.info("Transação atualizada: id={}, usuário={}", id, userId);
        return transactionMapper.toResponse(updatedTransaction);
    }

    @Override
    public void deleteTransaction(Integer id, Integer userId) {
        findUserById(userId);

        Transaction transaction =
                transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));

        validateTransactionOwnership(transaction, userId);

        transactionRepository.deleteById(id);
        log.info(
                "Transação deletada: id={}, tipo={}, valor={}, usuário={}",
                id,
                transaction.getType(),
                transaction.getAmount(),
                userId);
    }

    @Override
    public MonthlyBalanceResponse getMonthlyBalance(int year, int month, Integer userId) {
        findUserById(userId);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Transaction> userTransactions = transactionRepository.findAllByUserId(userId);

        BigDecimal totalIncome = calculateTotalByType(userTransactions, INCOME, startDate, endDate);
        BigDecimal totalExpense = calculateTotalByType(userTransactions, EXPENSE, startDate, endDate);
        BigDecimal balance = totalIncome.subtract(totalExpense);

        return MonthlyBalanceResponse.builder()
                .year(year)
                .month(month)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }

    private User findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private void validateTransactionOwnership(Transaction transaction, Integer userId) {
        if (transaction.getUser() == null || !transaction.getUser().getId().equals(userId)) {
            log.warn(
                    "Tentativa de acesso negado: Usuário {} tentou acessar transação {} que não lhe pertence",
                    userId,
                    transaction.getId());
            throw new TransactionAccessDeniedException(transaction.getId());
        }
    }

    private BigDecimal calculateTotalByType(
            List<Transaction> transactions, TransactionType type, LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .filter(t -> isDateInRange(t.getTransactionDate(), startDate, endDate))
                .map(Transaction::getAmount)
                .reduce(ZERO, BigDecimal::add);
    }

    private boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
