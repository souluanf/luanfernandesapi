package dev.luanfernandes.service;

import dev.luanfernandes.dto.request.UserRegisterRequest;
import dev.luanfernandes.dto.request.UserUpdateRequest;
import dev.luanfernandes.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse createUser(UserRegisterRequest request);

    List<UserResponse> listUsers();

    UserResponse getUserById(Integer id);

    void updateUser(Integer id, UserUpdateRequest request);

    void deleteUser(Integer id);
}
