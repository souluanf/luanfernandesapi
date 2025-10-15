package dev.luanfernandes.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import dev.luanfernandes.domain.entity.User;
import dev.luanfernandes.domain.enums.UserRole;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for UserRepositoryImpl")
class UserRepositoryImplTest {

    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl();
    }

    @Test
    @DisplayName("Should save new user and generate ID automatically")
    void shouldSaveNewUser_WhenIdIsNull() {
        var user = createUser("Test User", "test@example.com");

        var savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(1);
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should save multiple users with sequential IDs")
    void shouldSaveMultipleUsers_WithSequentialIds() {
        var user1 = createUser("User 1", "user1@example.com");
        var user2 = createUser("User 2", "user2@example.com");
        var user3 = createUser("User 3", "user3@example.com");

        var savedUser1 = userRepository.save(user1);
        var savedUser2 = userRepository.save(user2);
        var savedUser3 = userRepository.save(user3);

        assertThat(savedUser1.getId()).isEqualTo(1);
        assertThat(savedUser2.getId()).isEqualTo(2);
        assertThat(savedUser3.getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should update existing user when saving with existing ID")
    void shouldUpdateUser_WhenIdExists() {
        var user = createUser("Original Name", "original@example.com");
        var savedUser = userRepository.save(user);

        savedUser.setName("Updated Name");
        savedUser.setEmail("updated@example.com");
        var updatedUser = userRepository.save(savedUser);

        assertThat(updatedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");

        var foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Updated Name");
    }

    @Test
    @DisplayName("Should find all users successfully")
    void shouldFindAllUsers_Successfully() {
        var user1 = createUser("User 1", "user1@example.com");
        var user2 = createUser("User 2", "user2@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        var users = userRepository.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getName).containsExactlyInAnyOrder("User 1", "User 2");
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void shouldReturnEmptyList_WhenNoUsers() {
        var users = userRepository.findAll();

        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("Should delete user by ID successfully")
    void shouldDeleteUserById_Successfully() {
        var user = createUser("Test User", "test@example.com");
        var savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        var foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should find user by ID successfully")
    void shouldFindUserById_WhenUserExists() {
        var user = createUser("Test User", "test@example.com");
        var savedUser = userRepository.save(user);

        var foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getName()).isEqualTo("Test User");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should return empty when user not found by ID")
    void shouldReturnEmpty_WhenUserNotFound() {
        var foundUser = userRepository.findById(999);

        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should return true when user exists by email")
    void shouldReturnTrue_WhenUserExistsByEmail() {
        var user = createUser("Test User", "test@example.com");
        userRepository.save(user);

        var exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when user does not exist by email")
    void shouldReturnFalse_WhenUserDoesNotExistByEmail() {
        var exists = userRepository.existsByEmail("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return true when user exists by ID")
    void shouldReturnTrue_WhenUserExistsById() {
        var user = createUser("Test User", "test@example.com");
        var savedUser = userRepository.save(user);

        var exists = userRepository.existsById(savedUser.getId());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when user does not exist by ID")
    void shouldReturnFalse_WhenUserDoesNotExistById() {
        var exists = userRepository.existsById(999);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should handle multiple users with different emails correctly")
    void shouldHandleMultipleUsers_WithDifferentEmails() {
        var user1 = createUser("User 1", "user1@example.com");
        var user2 = createUser("User 2", "user2@example.com");
        var user3 = createUser("User 3", "user3@example.com");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        assertThat(userRepository.existsByEmail("user1@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("user2@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("user3@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }

    @Test
    @DisplayName("Should maintain data integrity after delete and save operations")
    void shouldMaintainDataIntegrity_AfterDeleteAndSave() {
        var user1 = createUser("User 1", "user1@example.com");
        var user2 = createUser("User 2", "user2@example.com");

        var savedUser1 = userRepository.save(user1);
        var savedUser2 = userRepository.save(user2);

        userRepository.deleteById(savedUser1.getId());

        var users = userRepository.findAll();
        assertThat(users).hasSize(1);
        assertThat(users.getFirst().getId()).isEqualTo(savedUser2.getId());

        var newUser = createUser("User 3", "user3@example.com");
        var savedUser3 = userRepository.save(newUser);

        assertThat(savedUser3.getId()).isEqualTo(3);
    }

    private User createUser(String name, String email) {
        User user = new User();
        user.setId(null);
        user.setName(name);
        user.setEmail(email);
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
