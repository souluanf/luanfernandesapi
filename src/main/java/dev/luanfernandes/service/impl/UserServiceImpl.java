package dev.luanfernandes.service.impl;

import dev.luanfernandes.domain.entity.User;
import dev.luanfernandes.domain.exception.UserAlreadyExistsException;
import dev.luanfernandes.domain.exception.UserHasTransactionsException;
import dev.luanfernandes.domain.exception.UserNotFoundException;
import dev.luanfernandes.dto.mapper.UserMapper;
import dev.luanfernandes.dto.request.UserRegisterRequest;
import dev.luanfernandes.dto.request.UserUpdateRequest;
import dev.luanfernandes.dto.response.UserResponse;
import dev.luanfernandes.repository.TransactionRepository;
import dev.luanfernandes.repository.UserRepository;
import dev.luanfernandes.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        log.info("Usuário criado: email={}, role={}", request.email(), request.role());
        return userMapper.toResponse(savedUser);
    }

    @Override
    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    @Override
    public void updateUser(Integer id, UserUpdateRequest request) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (request.email() != null
                && !request.email().equals(existingUser.getEmail())
                && userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }

        userMapper.updateEntityFromRequest(request, existingUser);
        userRepository.save(existingUser);
        log.info("Usuário atualizado: id={}", id);
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (transactionRepository.existsByUserId(id)) {
            throw new UserHasTransactionsException(id);
        }

        userRepository.deleteById(id);
        log.info("Usuário deletado: id={}, email={}", id, user.getEmail());
    }
}
