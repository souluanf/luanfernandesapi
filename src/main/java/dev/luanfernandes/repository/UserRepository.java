package dev.luanfernandes.repository;

import dev.luanfernandes.domain.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    List<User> findAll();

    void deleteById(Integer id);

    Optional<User> findById(Integer id);

    boolean existsByEmail(String email);

    boolean existsById(Integer id);
}
