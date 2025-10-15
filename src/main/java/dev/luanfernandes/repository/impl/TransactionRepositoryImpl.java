package dev.luanfernandes.repository.impl;

import dev.luanfernandes.domain.entity.Transaction;
import dev.luanfernandes.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class TransactionRepositoryImpl implements TransactionRepository {

    private final Map<Integer, Transaction> mapa = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction.getId() == null) {
            transaction.setId(nextId.getAndIncrement());
        }
        mapa.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(mapa.values());
    }

    @Override
    public void deleteById(Integer id) {
        mapa.remove(id);
    }

    @Override
    public Optional<Transaction> findById(Integer id) {
        return Optional.ofNullable(mapa.get(id));
    }

    @Override
    public List<Transaction> findAllByUserId(Integer userId) {
        return mapa.values().stream()
                .filter(transaction -> transaction.getUser() != null
                        && transaction.getUser().getId().equals(userId))
                .toList();
    }

    @Override
    public boolean existsByUserId(Integer userId) {
        return mapa.values().stream()
                .anyMatch(transaction -> transaction.getUser() != null
                        && transaction.getUser().getId().equals(userId));
    }
}
