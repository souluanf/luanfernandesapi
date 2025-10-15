package dev.luanfernandes.service.impl;

import static dev.luanfernandes.domain.enums.UserRole.ADMIN;
import static dev.luanfernandes.domain.enums.UserRole.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
@DisplayName("Tests for UserServiceImpl")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should create user successfully when valid data provided")
    void shouldCreateUser_WhenValidData() {
        var request = new UserRegisterRequest("Test User", "test@example.com", USER);
        var user = createTestUser();
        var savedUser = createTestUser();
        var expectedResponse = createUserResponse();

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(expectedResponse);

        var result = userService.createUser(request);

        assertThat(result).isEqualTo(expectedResponse);
        verify(userRepository).existsByEmail(request.email());
        verify(userMapper).toEntity(request);
        verify(userRepository).save(user);
        verify(userMapper).toResponse(savedUser);
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email already exists")
    void shouldThrowException_WhenEmailAlreadyExists() {
        var request = new UserRegisterRequest("Existing User", "existing@example.com", USER);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request)).isInstanceOf(UserAlreadyExistsException.class);

        verify(userRepository).existsByEmail(request.email());
    }

    @Test
    @DisplayName("Should list all users successfully")
    void shouldListUsers_Successfully() {
        var users = List.of(createTestUser());

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponse(any(User.class))).thenReturn(createUserResponse());

        var result = userService.listUsers();

        assertThat(result).hasSize(1);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserById_Successfully() {
        var userId = 1;
        var user = createTestUser();
        user.setId(userId);
        var expectedResponse = createUserResponse();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);

        var result = userService.getUserById(userId);

        assertThat(result).isEqualTo(expectedResponse);
        verify(userRepository).findById(userId);
        verify(userMapper).toResponse(user);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found")
    void shouldThrowException_WhenUserNotFound() {
        var userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId)).isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUser_Successfully() {
        var userId = 1;
        var request = new UserUpdateRequest("New Name", "newemail@example.com", USER);
        var existingUser = createTestUser();
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("newemail@example.com")).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.updateUser(userId, request);

        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmail("newemail@example.com");
        verify(userMapper).updateEntityFromRequest(request, existingUser);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when updating to existing email")
    void shouldThrowException_WhenUpdatingToExistingEmail() {
        var userId = 1;
        var request = new UserUpdateRequest(null, "existing@example.com", null);
        var existingUser = createTestUser();
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(userId, request))
                .isInstanceOf(UserAlreadyExistsException.class);

        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmail("existing@example.com");
    }

    @Test
    @DisplayName("Should update user successfully when email is null")
    void shouldUpdateUser_WhenEmailIsNull() {
        var userId = 1;
        var request = new UserUpdateRequest("New Name", null, ADMIN);
        var existingUser = createTestUser();
        existingUser.setId(userId);
        existingUser.setEmail("current@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.updateUser(userId, request);

        verify(userRepository).findById(userId);
        verify(userMapper).updateEntityFromRequest(request, existingUser);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Should update user successfully when email is the same as current")
    void shouldUpdateUser_WhenEmailIsSameAsCurrent() {
        var userId = 1;
        var currentEmail = "current@example.com";
        var request = new UserUpdateRequest("New Name", currentEmail, ADMIN);
        var existingUser = createTestUser();
        existingUser.setId(userId);
        existingUser.setEmail(currentEmail);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.updateUser(userId, request);

        verify(userRepository).findById(userId);
        verify(userMapper).updateEntityFromRequest(request, existingUser);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Should update only name when email is null and role is null")
    void shouldUpdateUser_WhenOnlyNameProvided() {
        var userId = 1;
        var request = new UserUpdateRequest("Updated Name", null, null);
        var existingUser = createTestUser();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.updateUser(userId, request);

        verify(userRepository).findById(userId);
        verify(userMapper).updateEntityFromRequest(request, existingUser);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Should delete user successfully when no transactions exist")
    void shouldDeleteUser_WhenNoTransactions() {
        var userId = 1;
        var user = createTestUser();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.existsByUserId(userId)).thenReturn(false);

        userService.deleteUser(userId);

        verify(userRepository).findById(userId);
        verify(transactionRepository).existsByUserId(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw UserHasTransactionsException when user has transactions")
    void shouldThrowException_WhenUserHasTransactions() {
        var userId = 1;
        var user = createTestUser();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.existsByUserId(userId)).thenReturn(true);

        assertThatThrownBy(() -> userService.deleteUser(userId)).isInstanceOf(UserHasTransactionsException.class);

        verify(userRepository).findById(userId);
        verify(transactionRepository).existsByUserId(userId);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when deleting non-existent user")
    void shouldThrowException_WhenDeletingNonExistentUser() {
        var userId = 999;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(userId)).isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRole(USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    private UserResponse createUserResponse() {
        return new UserResponse(1, "Test User", "test@example.com", USER, LocalDateTime.now(), LocalDateTime.now());
    }
}
