package dev.luanfernandes.controller;

import static dev.luanfernandes.constants.PathConstants.USERS;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import dev.luanfernandes.dto.request.UserRegisterRequest;
import dev.luanfernandes.dto.request.UserUpdateRequest;
import dev.luanfernandes.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
@RequestMapping(value = USERS, produces = APPLICATION_JSON_VALUE)
public interface UserController {

    @Operation(summary = "Criar usuário", description = "Cria novo usuário")
    @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou usuário já existe")
    @ApiResponse(responseCode = "409", description = "Usuário já existe com este email")
    @PostMapping
    ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRegisterRequest request);

    @Operation(summary = "Listar usuários", description = "Lista todos os usuários do sistema")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários retornada com sucesso",
            content =
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
    @GetMapping
    ResponseEntity<List<UserResponse>> listUsers();

    @Operation(summary = "Buscar usuário por ID", description = "Busca usuário específico por ID.")
    @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(@PathVariable Integer id);

    @Operation(summary = "Atualizar usuário", description = "Atualiza dados do usuário")
    @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @PutMapping("/{id}")
    ResponseEntity<Void> updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateRequest request);

    @Operation(summary = "Deletar usuário", description = "Deleta usuário.")
    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "409", description = "Não é possível deletar usuário com transações associadas")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Integer id);
}
