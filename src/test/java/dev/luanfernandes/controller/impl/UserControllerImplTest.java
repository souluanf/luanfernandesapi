package dev.luanfernandes.controller.impl;

import static dev.luanfernandes.domain.enums.UserRole.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import dev.luanfernandes.dto.request.UserRegisterRequest;
import dev.luanfernandes.dto.request.UserUpdateRequest;
import dev.luanfernandes.dto.response.UserResponse;
import dev.luanfernandes.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for UserControllerImpl")
class UserControllerImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserControllerImpl userController;

    @Test
    @DisplayName("Should create user successfully when valid data provided")
    void shouldCreateUser_WhenValidData() {
        var request = new UserRegisterRequest("Test User", "test@example.com", USER);
        var expectedResponse = createUserResponse();

        when(userService.createUser(any(UserRegisterRequest.class))).thenReturn(expectedResponse);

        var result = userController.createUser(request);

        assertThat(result.getStatusCode()).isEqualTo(CREATED);
        assertThat(result.getBody()).isEqualTo(expectedResponse);
        verify(userService).createUser(request);
    }

    @Test
    @DisplayName("Should list all users successfully")
    void shouldListUsers_Successfully() {
        var userResponse = createUserResponse();
        var expectedList = List.of(userResponse);

        when(userService.listUsers()).thenReturn(expectedList);

        var result = userController.listUsers();

        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody()).isEqualTo(expectedList);
        verify(userService).listUsers();
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void shouldReturnEmptyList_WhenNoUsersExist() {
        when(userService.listUsers()).thenReturn(List.of());

        var result = userController.listUsers();

        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody()).isEmpty();
        verify(userService).listUsers();
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserById_WhenValidId() {
        var userId = 1;
        var userResponse = createUserResponse();

        when(userService.getUserById(userId)).thenReturn(userResponse);

        var result = userController.getUserById(userId);

        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody()).isEqualTo(userResponse);
        verify(userService).getUserById(userId);
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUser_WhenValidData() {
        var userId = 1;
        var updateRequest = new UserUpdateRequest("Updated Name", "newemail@example.com", USER);

        doNothing().when(userService).updateUser(userId, updateRequest);

        var result = userController.updateUser(userId, updateRequest);

        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(result.getBody()).isNull();
        verify(userService).updateUser(userId, updateRequest);
    }

    @Test
    @DisplayName("Should update user with partial data")
    void shouldUpdateUser_WhenPartialData() {
        var userId = 1;
        var updateRequest = new UserUpdateRequest(null, "newemail@example.com", null);

        doNothing().when(userService).updateUser(userId, updateRequest);

        var result = userController.updateUser(userId, updateRequest);

        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(result.getBody()).isNull();
        verify(userService).updateUser(userId, updateRequest);
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUser_WhenValidId() {
        var userId = 1;

        doNothing().when(userService).deleteUser(userId);

        var result = userController.deleteUser(userId);

        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(result.getBody()).isNull();
        verify(userService).deleteUser(userId);
    }

    private UserResponse createUserResponse() {
        return new UserResponse(1, "Test User", "test@example.com", USER, LocalDateTime.now(), LocalDateTime.now());
    }
}
