package dev.luanfernandes.controller.impl;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import dev.luanfernandes.controller.UserController;
import dev.luanfernandes.dto.request.UserRegisterRequest;
import dev.luanfernandes.dto.request.UserUpdateRequest;
import dev.luanfernandes.dto.response.UserResponse;
import dev.luanfernandes.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> createUser(UserRegisterRequest request) {
        return status(CREATED).body(userService.createUser(request));
    }

    @Override
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ok(userService.listUsers());
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(Integer id) {
        return ok(userService.getUserById(id));
    }

    @Override
    public ResponseEntity<Void> updateUser(Integer id, UserUpdateRequest request) {
        userService.updateUser(id, request);
        return noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(Integer id) {
        userService.deleteUser(id);
        return noContent().build();
    }
}
