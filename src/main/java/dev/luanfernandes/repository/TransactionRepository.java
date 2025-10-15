package dev.luanfernandes.repository;

import dev.luanfernandes.domain.entity.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    List<Transaction> findAll();

    void deleteById(Integer id);

    Optional<Transaction> findById(Integer id);

    List<Transaction> findAllByUserId(Integer userId);

    boolean existsByUserId(Integer userId);
}
