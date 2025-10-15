package dev.luanfernandes.repository.impl;

import dev.luanfernandes.domain.entity.User;
import dev.luanfernandes.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryImpl implements UserRepository {

    private final Map<Integer, User> mapa = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(nextId.getAndIncrement());
        }
        mapa.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(mapa.values());
    }

    @Override
    public void deleteById(Integer id) {
        mapa.remove(id);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(mapa.get(id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return mapa.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsById(Integer id) {
        return mapa.containsKey(id);
    }
}
